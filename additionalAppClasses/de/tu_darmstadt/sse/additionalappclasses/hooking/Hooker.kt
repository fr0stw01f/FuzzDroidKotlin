package de.tu_darmstadt.sse.additionalappclasses.hooking

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.net.URI

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.Signature
import android.util.Log

import com.morgoo.hook.zhook.MethodHook
import com.morgoo.hook.zhook.MethodHook.MethodHookParam
import com.morgoo.hook.zhook.ZHook

import de.tu_darmstadt.sse.additionalappclasses.tracing.BytecodeLogger
import de.tu_darmstadt.sse.additionalappclasses.util.UtilHook
import de.tu_darmstadt.sse.sharedclasses.SharedClassesSettings
import de.tu_darmstadt.sse.sharedclasses.networkconnection.FileFormat
import de.tu_darmstadt.sse.sharedclasses.networkconnection.NetworkConnectionInitiator
import de.tu_darmstadt.sse.sharedclasses.networkconnection.serializables.FileFuzzingSerializableObject
import de.tu_darmstadt.sse.sharedclasses.networkconnection.serializables.SignatureSerializableObject
import de.tu_darmstadt.sse.sharedclasses.util.Pair


object Hooker {

    lateinit var applicationContext: Context

    fun initializeHooking(context: Context) {
        applicationContext = context
        Log.i("SSE", "Initialize hooker...")
        NetworkConnectionInitiator.initNetworkConnection()
        BytecodeLogger.initialize(context)
        Hooker.doHooking(UtilHook.initAllHookers())
        Log.i("SSE", "Hooking ready...")
    }

    fun doHooking(infos: Set<HookInfo>) {
        for (info in infos) {
            if (info is MethodHookInfo)
                doMethodHooking(info)
            else if (info is FieldHookInfo)
                doFieldHooking(info)
        }


    }

    private fun doMethodHooking(info: MethodHookInfo) {
        val callback = object : MethodHook() {

            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)

                if (info.hasHookBefore()) {
                    val orderedBeforeHooks = info.beforeHooks

                    for (singleBeforeHook in orderedBeforeHooks!!) {

                        var hookingType: String? = null
                        // adding the runtime values of the params before hook
                        if (singleBeforeHook is AnalysisDependentMethodHookBefore) {
                            hookingType = "AnalysisDependentMethodHookBefore"
                            singleBeforeHook.retrieveValueFromServer(param.args)
                        } else if (singleBeforeHook is ConditionalMethodHookBefore) {
                            //							Log.i(SharedClassesSettings.TAG, "in ConditionalMethodHookBefore");
                            hookingType = "ConditionalMethodHookBefore"
                            singleBeforeHook.testConditionSatisfaction(param)
                        } else if (singleBeforeHook is DexFileExtractorHookBefore) {
                            Log.i(SharedClassesSettings.TAG, "in DexFileExtractorHookBefore")
                            hookingType = "DexFileExtractorHookBefore"
                            val dexFileHook = singleBeforeHook
                            val argumentPos = dexFileHook.argumentPosition
                            val dexFilePath = param.args[argumentPos] as String
                            dexFileHook.sendDexFileToServer(dexFilePath)
                        }

                        // first match of hooks quits the hooking
                        if (singleBeforeHook.isValueReplacementNecessary()!!) {

                            //only for logging purpose
                            Log.i(SharedClassesSettings.TAG, String.format("[HOOK] %s || MethodSign: %s || Replace: %s", hookingType, param.method.toString(), singleBeforeHook.getParamValuesToReplace()))

                            // change only those params that are required
                            for (paramValuePair in singleBeforeHook.getParamValuesToReplace()!!) {
                                //special handling for file fuzzing:
                                if (paramValuePair.getSecond() is FileFuzzingSerializableObject)
                                    doFileFuzzingIfNecessary(param, paramValuePair.getFirst()!!, paramValuePair.getSecond()!! as FileFuzzingSerializableObject)
                                else
                                    param.args[paramValuePair.getFirst()!!] = paramValuePair.getSecond()
                            }
                            return
                        }
                    }
                }
            }

            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)

                if (info.hasHookAfter()) {
                    val orderedAfterHooks = info.afterHooks

                    for (singleAfterHook in orderedAfterHooks!!) {
                        var hookingType: String? = null
                        // adding the runtime value of the return value
                        if (singleAfterHook is AnalysisDependentMethodHookAfter) {
                            hookingType = "AnalysisDependentMethodHookAfter"
                            singleAfterHook.retrieveValueFromServer(param.result)
                        } else if (singleAfterHook is ConditionalMethodHookAfter) {
                            hookingType = "ConditionalMethodHookAfter"
                            singleAfterHook.testConditionSatisfaction(param)
                        } else if (singleAfterHook is SimpleBooleanHookAfter) {
                            hookingType = "SimpleBooleanHookAfter"
                            singleAfterHook.retrieveBooleanValueFromServer()
                        }

                        // first match of hooks quits the hooking
                        if (singleAfterHook.isValueReplacementNecessary()!!) {
                            //this is a hardcoded check due to serialization problems with the non-serializable PackageInfo object
                            val returnValue = singleAfterHook.getReturnValue()
                            if (returnValue is SignatureSerializableObject) {
                                Log.i(SharedClassesSettings.TAG, "TEST: SignatureSerializableObject")
                                if (param.result is PackageInfo) {
                                    Log.i(SharedClassesSettings.TAG, "TEST: PackageInfo")
                                    val pm = param.result as PackageInfo
                                    pm.signatures[0] = Signature(returnValue.encodedCertificate)

                                    //only for logging purpose
                                    Log.i(SharedClassesSettings.TAG, String.format("[HOOK] %s || MethodSign: %s || Replace: %s", hookingType, param.method.toString(), pm.signatures[0]))
                                }
                            } else {
                                param.result = returnValue
                                //only for logging purpose
                                Log.i(SharedClassesSettings.TAG, String.format("[HOOK] %s || MethodSign: %s || Replace: %s", hookingType, param.method.toString(), singleAfterHook.getReturnValue()))
                            }
                            return
                        }
                    }
                }
            }
        }

        var methodOrConstructor: Member? = null
        try {
            val cls = Class.forName(info.className)
            val tmp = info.params
            if (info.methodName == "<init>") {
                methodOrConstructor = cls.getDeclaredConstructor(*tmp!!)
            } else {
                if (tmp == null)
                    methodOrConstructor = cls.getDeclaredMethod(info.methodName)
                else
                    methodOrConstructor = cls.getDeclaredMethod(info.methodName, *tmp)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ZHook.hookMethod(methodOrConstructor, callback)
    }


    private fun doFieldHooking(info: FieldHookInfo) {
        val afterHook = info.afterHook

        if (afterHook!!.isValueReplacementNecessary()!!) {
            try {
                val tmp = Class.forName(info.className)
                val field = tmp.getField(info.fieldName)
                field.isAccessible = true
                val oldValue = retrieveOldFieldValue(info.className, info.fieldName)
                // Sets the field to the new value
                field.set(oldValue, afterHook.getNewValue())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun retrieveOldFieldValue(className: String, fieldName: String): Any? {
        try {
            val tmp = Class.forName(className)
            val field = tmp.getField(fieldName)
            field.isAccessible = true
            return field.get(Class.forName(className))
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }

    }

    private fun doFileFuzzingIfNecessary(param: MethodHookParam, index: Int, fuzzyingObject: FileFuzzingSerializableObject) {
        //file is in private dir and only the file name is necessary
        if (fuzzyingObject.storageMode == 0)
            doPrivateDirFileFuzzing(param, index, fuzzyingObject)
        else if (fuzzyingObject.storageMode == 1)
            doFileFuzzingBasedOnFileObject(param, fuzzyingObject)
        else if (fuzzyingObject.storageMode == 2)
            doFileFuzzingBasedOnAbsoluteStringPath(param, index, fuzzyingObject)//absolute path of file is given
        //there is a file object available
    }


    private fun doFileFuzzingBasedOnAbsoluteStringPath(param: MethodHookParam, index: Int, fuzzyingObject: FileFuzzingSerializableObject) {
        //get file
        val file = File(param.args[index] as String)

        //second copy corresponding file to file path
        copyCorrectFile(file, fuzzyingObject.fileFormat)
    }


    private fun doFileFuzzingBasedOnFileObject(param: MethodHookParam, fuzzyingObject: FileFuzzingSerializableObject) {
        var file: File? = null
        //first get correct file
        if (param.args.size == 2) {
            //File(File parent, String child)
            if (param.args[0] is File && param.args[1] is String)
                file = File(param.args[0] as File, param.args[1] as String)
            else if (param.args[0] is String && param.args[1] is String)
                file = File(param.args[0] as String, param.args[1] as String)
            else
                return //File(String parent, String child)

        } else if (param.args.size == 1) {
            //File(String pathname)
            if (param.args[0] is String)
                file = File(param.args[0] as String)
            else if (param.args[0] is URI)
                file = File(param.args[0] as URI)
            else
                return //File(URI uri)
        }

        //there is nothing to do
        if (file == null || file.exists())
            return

        //second copy corresponding file to file path
        copyCorrectFile(file, fuzzyingObject.fileFormat)
    }


    private fun doPrivateDirFileFuzzing(param: MethodHookParam, index: Int, fuzzyingObject: FileFuzzingSerializableObject) {
        val fileName = param.args[index] as String
        val appContext = Hooker.applicationContext
        val localFile = appContext.getFileStreamPath(fileName)
        //only create a dummy file if there is no file
        if (!localFile.exists()) {
            //what file format do we need?
            copyCorrectFile(localFile, fuzzyingObject.fileFormat)
        }
    }


    private fun copyCorrectFile(localFile: File, fileFormat: FileFormat) {
        var sdCardFilePath: String? = null

        if (fileFormat == FileFormat.DIRECTORY) {
            if (!localFile.exists()) {
                localFile.mkdir()
            }
        } else {
            if (fileFormat == FileFormat.PROPERTIES)
                sdCardFilePath = SharedClassesSettings.FUZZY_FILES_DIR_PATH + "/properties.properties"
            else if (fileFormat == FileFormat.UNKNOWN)
                sdCardFilePath = SharedClassesSettings.FUZZY_FILES_DIR_PATH + "/text.text"
            else if (fileFormat == FileFormat.DEX)
                sdCardFilePath = SharedClassesSettings.FUZZY_FILES_DIR_PATH + "/dex.dex"
            else if (fileFormat == FileFormat.DATABASE)
                sdCardFilePath = SharedClassesSettings.FUZZY_FILES_DIR_PATH + "/db.db"

            val sdCardFile = File(sdCardFilePath!!)
            try {
                val `in` = FileInputStream(sdCardFile)
                val out = FileOutputStream(localFile)

                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len = `in`.read(buf)
                while (len > 0) {
                    out.write(buf, 0, len)
                    len = `in`.read(buf)
                }
                `in`.close()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}
