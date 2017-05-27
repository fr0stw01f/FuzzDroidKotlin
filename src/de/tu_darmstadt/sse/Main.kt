package de.tu_darmstadt.sse

import com.android.ddmlib.AndroidDebugBridge
import de.tu_darmstadt.sse.apkspecific.CodeModel.CodePositionManager
import de.tu_darmstadt.sse.apkspecific.UtilApk
import de.tu_darmstadt.sse.appinstrumentation.Instrumenter
import de.tu_darmstadt.sse.appinstrumentation.UtilInstrumenter
import de.tu_darmstadt.sse.appinstrumentation.transformer.InstrumentedCodeTag
import de.tu_darmstadt.sse.bootstrap.AnalysisTask
import de.tu_darmstadt.sse.bootstrap.AnalysisTaskManager
import de.tu_darmstadt.sse.bootstrap.DexFileManager
import de.tu_darmstadt.sse.commandlinelogger.LoggerHelper
import de.tu_darmstadt.sse.commandlinelogger.MyLevel
import de.tu_darmstadt.sse.decisionmaker.DecisionMaker
import de.tu_darmstadt.sse.decisionmaker.DecisionMakerConfig
import de.tu_darmstadt.sse.decisionmaker.DeterministicRandom
import de.tu_darmstadt.sse.decisionmaker.UtilDecisionMaker
import de.tu_darmstadt.sse.frameworkevents.FrameworkEvent
import de.tu_darmstadt.sse.frameworkevents.manager.FrameworkEventManager
import org.xmlpull.v1.XmlPullParserException
import soot.PackManager
import soot.Scene
import soot.SootMethod
import soot.Unit
import soot.jimple.infoflow.android.SetupApplication
import soot.jimple.infoflow.android.config.SootConfigForAndroid
import soot.jimple.infoflow.cfg.LibraryClassPatcher
import soot.jimple.infoflow.solver.cfg.BackwardsInfoflowCFG
import soot.jimple.infoflow.source.data.NullSourceSinkDefinitionProvider
import soot.options.Options
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.logging.Level


class Main private constructor() {


    @Throws(IOException::class, XmlPullParserException::class)
    fun run(args: Array<String>): Set<EnvironmentResult>? {
        val frameworkOptions = FrameworkOptions()
        frameworkOptions.parse(args)
        LoggerHelper.initialize(FrameworkOptions.apkPath)
        LoggerHelper.logEvent(MyLevel.APKPATH, FrameworkOptions.apkPath)
        LoggerHelper.logEvent(MyLevel.ANALYSIS,
                String.format("Force timeout: %d || Ininactivity timeout: %d || Max restarts: %d",
                        FrameworkOptions.forceTimeout,
                        FrameworkOptions.inactivityTimeout,
                        FrameworkOptions.maxRestarts))


        //remove all files in sootOutput folder
        org.apache.commons.io.FileUtils.cleanDirectory(File(UtilInstrumenter.SOOT_OUTPUT))

        val blacklistedAPKs = UtilMain.blacklistedAPKs

        if (!blacklistedAPKs.contains(FrameworkOptions.apkPath)) {
            val dexFileManager = DexFileManager()

            // Schedule the initial analysis task
            val analysisTaskManager = AnalysisTaskManager()
            analysisTaskManager.enqueueAnalysisTask(AnalysisTask())

            // Execute all of our analysis tasks
            val results = HashSet<EnvironmentResult>()
            val currentTask = analysisTaskManager.scheduleNextTask()
            var taskId = 0
            while (currentTask != null) {
                LoggerHelper.logInfo(String.format("Starting analysis for task %d with %d dex files",
                        taskId++, currentTask.dexFilesToMerge.size))
                try {
                    //needed for the extractAllTargetLocations method
                    initializeSoot(currentTask)
                } catch (ex: Exception) {
                    System.err.println("Could not initialize Soot, skipping analysis task: " + ex.message)
                    ex.printStackTrace()
                    continue
                }

                // The DecisionMaker configuration needs to initialize the metrics.
                // Therefore, it requires a running Soot instance.
                var config = DecisionMakerConfig()
                config.initializeCFG()

                //extract all target locations
                val allTargetLocations = UtilDecisionMaker.extractAllTargetLocations()
                if (allTargetLocations.isEmpty()) {
                    LoggerHelper.logEvent(MyLevel.NO_TARGETS, "There are no reachable target locations")
                    UtilMain.writeToFile("noLoggingPoint.txt", FrameworkOptions.apkPath + "\n")
                }

                //we have to do this hack due to a reset of soot. This step is necessary otherwise, we will not get a clean apk for each logging point
                val targetsAsCodePos = UtilMain.convertUnitsToIndependentCodePosition(allTargetLocations, config.backwardsCFG!!)
                var firstRun = true

                //treat every target location individually
                for (singleTargetAsPos in targetsAsCodePos) {
                    if (!firstRun)
                        initializeSoot(currentTask)
                    firstRun = false

                    //we need to do this step, because we reseted soot
                    val singleTarget = UtilMain.convertIndependentCodePositionToUnits(singleTargetAsPos)
                    if (singleTarget == null) {
                        LoggerHelper.logEvent(MyLevel.EXCEPTION_ANALYSIS, "############ PLEASE DOUBLE CHECK TARGET LOCATION ")
                        continue
                    }

                    val singleTargetLocation = setOf(singleTarget)

                    // We may need to remove some statements before running the analysis
                    removeStatementsForAnalysis(currentTask)

                    // needs to be re-initialized due to soot-reset
                    config = DecisionMakerConfig()
                    config.initializeCFG()

                    //now we have access to the CFG
                    //check if target is reachable:
                    if (config.backwardsCFG!!.getMethodOf(singleTarget) == null) {
                        LoggerHelper.logEvent(MyLevel.LOGGING_POINT, "target is not statically reachable!")
                        continue
                    }

                    val successfulInitialization = config.initialize(singleTargetLocation)

                    if (successfulInitialization) {
                        //get potential Android event which trigger the initial code section for reaching the logging point
                        var events: MutableSet<FrameworkEvent>?
                        //todo PAPER-EVAL ONLY
                        if (FrameworkOptions.evaluationJustStartApp) {
                            events = HashSet<FrameworkEvent>()
                        } else
                            events = getFrameworkEvents(singleTarget, config.backwardsCFG!!)
                        if (events.isEmpty()) {
                            LoggerHelper.logEvent(MyLevel.ADB_EVENT, "no events available")
                        }

                        val decisionMaker = DecisionMaker(config, dexFileManager, analysisTaskManager)
                        val codePositionManager = CodePositionManager.codePositionManagerInstance

                        appPreparationPhase(codePositionManager!!, config)

                        val totalEvents = events.size
                        var currentEvent = 0
                        val resultsPerApp = HashSet<EnvironmentResult>()
                        for (event in events) {
                            try {
                                currentEvent += 1

                                //todo PAPER-EVAL ONLY
                                if (!FrameworkOptions.evaluationOnly)
                                    decisionMaker.runPreAnalysisPhase()

                                //after the pre-analysis, we are able to get access to the code positions
                                //let's log the code position
                                val codePos = codePositionManager.getCodePositionForUnit(singleTarget)
                                val info = String.format("Enclosing Method: %s | %d | %s", codePos?.enclosingMethod, codePos?.id, singleTarget.toString())
                                LoggerHelper.logEvent(MyLevel.LOGGING_POINT, info)

                                if (events.isEmpty())
                                    LoggerHelper.logEvent(Level.INFO, String.format("%s: 0/0 events sent", codePos?.id))
                                else
                                    LoggerHelper.logEvent(Level.INFO, String.format("%s: %s/%s events (%s) ready for process", codePos?.id, currentEvent, totalEvents, event))

                                LoggerHelper.logEvent(MyLevel.EXECUTION_START, "")
                                repeatedlyExecuteAnalysis(decisionMaker, resultsPerApp, event)
                                LoggerHelper.logEvent(MyLevel.EXECUTION_STOP, "")

                                // If we have reached the goal, there is no need to try the other events
                                for (result in resultsPerApp)
                                    if (result.isTargetReached)
                                        break
                            } catch (ex: Exception) {
                                LoggerHelper.logEvent(MyLevel.EXCEPTION_RUNTIME, ex.message!!)
                                ex.printStackTrace()
                            }

                        }

                        results.addAll(resultsPerApp)
                    }
                }
            }
            return results
        }
        return null
    }


    private fun removeStatementsForAnalysis(currentTask: AnalysisTask) {
        for (codePos in currentTask.statementsToRemove) {
            // Get the method in which to remove the statement
            val sm = Scene.v().grabMethod(codePos.methodSignature)
            if (sm == null) {
                LoggerHelper.logWarning("Method " + codePos.methodSignature + " not found")
                continue
            }

            var lineNum = 0
            val unitIt = sm.activeBody.units.iterator()
            while (unitIt.hasNext()) {
                // Is this the statement to remove?
                val u = unitIt.next()

                // Ignore statements that were added by an instrumenter
                if (!u.hasTag(InstrumentedCodeTag.name))
                    if (lineNum == codePos.lineNumber)
                        if (codePos.statement == null || u.toString() == codePos.statement) {
                            unitIt.remove()
                            break
                        }
                lineNum++
            }
        }
    }


    private fun appPreparationPhase(codePositionManager: CodePositionManager,
                                    config: DecisionMakerConfig) {
        LoggerHelper.logEvent(MyLevel.ANALYSIS, "Prepare app for fuzzing...")

        UtilApk.removeOldAPKs(FrameworkOptions.apkName)

        val instrumenter = Instrumenter(codePositionManager, config)
        LoggerHelper.logEvent(MyLevel.INSTRUMENTATION_START, "")
        instrumenter.doInstrumentation()
        LoggerHelper.logEvent(MyLevel.INSTRUMENTATION_STOP, "")

        if (FrameworkOptions.deployApp) {
            UtilApk.jarsigner(FrameworkOptions.apkName)
            UtilApk.zipalign(FrameworkOptions.apkName)
        }
    }

    private fun repeatedlyExecuteAnalysis(decisionMaker: DecisionMaker,
                                          results: MutableSet<EnvironmentResult>, event: FrameworkEvent) {
        decisionMaker.initialize()

        for (seed in 0..FrameworkOptions.nbSeeds - 1) {
            LoggerHelper.logEvent(MyLevel.ANALYSIS, "Running analysis with seed " + seed)
            DeterministicRandom.reinitialize(seed)
            val curResult = decisionMaker.executeDecisionMaker(event)

            results.add(curResult!!)
            if (curResult.isTargetReached)
                LoggerHelper.logEvent(MyLevel.RUNTIME, "target reached")
            else
                LoggerHelper.logEvent(MyLevel.RUNTIME, "NO target was reached")
        }
        decisionMaker.tearDown()
    }


    @Throws(IOException::class, XmlPullParserException::class)
    private fun initializeSoot(currentTask: AnalysisTask) {
        val app = SetupApplication(FrameworkOptions.androidJarPath,
                FrameworkOptions.apkPath)
        app.calculateSourcesSinksEntrypoints(NullSourceSinkDefinitionProvider())

        SootConfigForAndroid().setSootOptions(Options.v())

        Options.v().set_allow_phantom_refs(true)
        Options.v().set_validate(true)
        Options.v().set_force_overwrite(true)

        // We need a callgraph
        Options.v().set_whole_program(true)
        Options.v().setPhaseOption("cg.spark", "on")

        Options.v().set_src_prec(Options.src_prec_apk)
        Options.v().set_output_format(Options.output_format_dex)

        val processDir = ArrayList<String>()
        processDir.add(FrameworkOptions.apkPath)

        // dex files to merge
        for (dexFile in currentTask.dexFilesToMerge) {
            if (dexFile.localFileName != null
                    && !dexFile.localFileName.isEmpty()
                    && File(dexFile.localFileName).exists())
                processDir.add(dexFile.localFileName)
            else
                throw RuntimeException("Could not find local dex file")
        }

        //hooking specific
        processDir.add(UtilInstrumenter.HOOKING_LIBRARY)
        processDir.add(UtilInstrumenter.ADDITIONAL_APP_CLASSES_BIN)
        processDir.add(UtilInstrumenter.SHARED_CLASSES_BIN)
        Options.v().set_process_dir(processDir)

        Options.v().set_android_jars(FrameworkOptions.androidJarPath)
        Options.v().set_no_writeout_body_releasing(true)

        //the bin folder has to be added to the classpath in order to
        //use the Java part for the instrumentation (JavaClassForInstrumentation)
        val androidJar = Scene.v().getAndroidJarPath(FrameworkOptions.androidJarPath, FrameworkOptions.apkPath)
        Options.v().set_soot_classpath(UtilInstrumenter.ADDITIONAL_APP_CLASSES_BIN + File.pathSeparator +
                UtilInstrumenter.SHARED_CLASSES_BIN + File.pathSeparator + androidJar)

        Scene.v().loadNecessaryClasses()

        LibraryClassPatcher().patchLibraries()

        // Create the entry point
        app.entryPointCreator.setDummyMethodName("main")
        val entryPoint = app.entryPointCreator.createDummyMain()
        entryPoint.declaringClass.setLibraryClass()
        Options.v().set_main_class(entryPoint.declaringClass.name)
        Scene.v().entryPoints = listOf<SootMethod>(entryPoint)

        PackManager.v().runPacks()
    }


    private fun printTargetLocationInfo(config: DecisionMakerConfig, codePositionManager: CodePositionManager) {
        LoggerHelper.logEvent(MyLevel.ANALYSIS, "Found " + config.allTargetLocations!!.size + " target location(s)")
        for (unit in config.allTargetLocations!!) {
            val codePos = codePositionManager.getCodePositionForUnit(unit)
            val info = String.format("Enclosing Method: %s | %d | %s", codePos?.enclosingMethod, codePos?.id, unit.toString())
            LoggerHelper.logEvent(MyLevel.LOGGING_POINT, info)
        }
    }


    private fun getFrameworkEvents(targetLocation: Unit, cfg: BackwardsInfoflowCFG): MutableSet<FrameworkEvent> {
        val eventManager = FrameworkEventManager.eventManager
        val manifest = UtilApk.getManifest()
        val targetAndroidEvents = eventManager.extractInitalEventsForReachingTarget(targetLocation, cfg, manifest!!)
        return targetAndroidEvents
    }

    companion object {

        private val SINGLETON = Main()

        fun v(): Main {
            return SINGLETON
        }

        fun hexStringToByteArray(s: String): ByteArray {
            val len = s.length
            val data = ByteArray(len / 2)
            var i = 0
            while (i < len) {
                data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
                i += 2
            }
            return data
        }


        @JvmStatic fun main(args: Array<String>) {
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    LoggerHelper.logEvent(MyLevel.TIMEOUT, "-1 | Complete analysis stopped due to timeout of 40 minutes")
                    System.exit(0)
                }
            }, (40 * 60000).toLong())


            try {
                Main.v().run(args)
                AndroidDebugBridge.terminate()
            } catch (ex: Exception) {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                ex.printStackTrace(pw)
                LoggerHelper.logEvent(MyLevel.EXCEPTION_ANALYSIS, sw.toString())
                UtilMain.writeToFile("mainException.txt", FrameworkOptions.apkPath + "\n")
            }

            LoggerHelper.logEvent(MyLevel.EXECUTION_STOP, "Analysis successfully terminated")
            //this is necessary otherwise we will wait for a max of 20 minutes for the TimerTask
            System.exit(0)
        }
    }
}
