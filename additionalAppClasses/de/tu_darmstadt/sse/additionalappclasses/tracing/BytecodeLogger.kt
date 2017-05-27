package de.tu_darmstadt.sse.additionalappclasses.tracing

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.Queue
import java.util.concurrent.LinkedBlockingQueue

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import de.tu_darmstadt.sse.additionalappclasses.hooking.Hooker
import de.tu_darmstadt.sse.additionalappclasses.tracing.TracingService.TracingServiceBinder
import de.tu_darmstadt.sse.sharedclasses.SharedClassesSettings
import de.tu_darmstadt.sse.sharedclasses.dynamiccfg.MethodCallItem
import de.tu_darmstadt.sse.sharedclasses.dynamiccfg.MethodEnterItem
import de.tu_darmstadt.sse.sharedclasses.dynamiccfg.MethodLeaveItem
import de.tu_darmstadt.sse.sharedclasses.dynamiccfg.MethodReturnItem
import de.tu_darmstadt.sse.sharedclasses.networkconnection.IClientRequest
import de.tu_darmstadt.sse.sharedclasses.networkconnection.ServerCommunicator
import de.tu_darmstadt.sse.sharedclasses.tracing.DexFileTransferTraceItem
import de.tu_darmstadt.sse.sharedclasses.tracing.DynamicIntValueTraceItem
import de.tu_darmstadt.sse.sharedclasses.tracing.DynamicStringValueTraceItem
import de.tu_darmstadt.sse.sharedclasses.tracing.FileBasedTracingUtils
import de.tu_darmstadt.sse.sharedclasses.tracing.PathTrackingTraceItem
import de.tu_darmstadt.sse.sharedclasses.tracing.TargetReachedTraceItem
import de.tu_darmstadt.sse.sharedclasses.tracing.TimingBombTraceItem
import de.tu_darmstadt.sse.sharedclasses.tracing.TraceItem


class BytecodeLogger internal constructor()// this constructor shall just prevent external code from instantiating
// this class
{
    companion object {

        private val bootupQueue = LinkedBlockingQueue<TraceItem>()
        private var tracingService: ITracingServiceInterface? = null
        private val tracingConnection = object : ServiceConnection {

            override fun onServiceDisconnected(arg0: ComponentName) {
                tracingService = null
                Log.i(SharedClassesSettings.TAG, "Tracing service disconnected")
            }

            override fun onServiceConnected(arg0: ComponentName, serviceBinder: IBinder?) {
                Log.i(SharedClassesSettings.TAG, "Tracing service connected")
                if (serviceBinder == null) {
                    Log.e(SharedClassesSettings.TAG, "Got a null binder. Shitaki.")
                    return
                }

                try {
                    tracingService = (serviceBinder as TracingServiceBinder).service
                } catch (ex: RuntimeException) {
                    Log.e(SharedClassesSettings.TAG, "Could not get tracing service: " + ex.message)
                }

            }

        }


        fun initialize(context: Context) {
            // Start the service in its own thread to avoid an ANR
            if (tracingService == null) {
                val initThread = object : Thread() {

                    override fun run() {
                        if (tracingService == null) {
                            Log.i(SharedClassesSettings.TAG, "Binding to tracing service...")
                            val serviceIntent = Intent(context, TracingService::class.java)
                            serviceIntent.action = TracingService.ACTION_NULL
                            context.startService(serviceIntent)
                            if (context.bindService(serviceIntent, tracingConnection, Context.BIND_AUTO_CREATE))
                                Log.i(SharedClassesSettings.TAG, "Tracing service bound.")
                            else
                                Log.i(SharedClassesSettings.TAG, "bindService() returned false.")
                        }
                    }

                }
                initThread.start()
            }
        }

        private val lastExecutedStatement = object : ThreadLocal<Int>() {

            override fun initialValue(): Int? {
                return -1
            }

        }


        private var globalLastExecutedStatement: Int = 0


        fun setLastExecutedStatement(statementID: Int) {
            lastExecutedStatement.set(statementID)
            globalLastExecutedStatement = statementID
        }


        fun getLastExecutedStatement(): Int {
            return lastExecutedStatement.get()
        }


        private val appContext: Context
            get() {
                if (Hooker.applicationContext != null)
                    return Hooker.applicationContext
                try {
                    val activityThreadClass = Class.forName("android.app.ActivityThread")
                    var method = activityThreadClass.getMethod("currentApplication")
                    var app: Context? = method.invoke(null, null as Array<Any>?) as Context

                    if (app == null) {
                        val appGlobalsClass = Class.forName("android.app.AppGlobals")
                        method = appGlobalsClass.getMethod("getInitialApplication")
                        app = method.invoke(null, null as Array<Any>?) as Context
                    }

                    return app
                } catch (ex: Exception) {
                    throw RuntimeException("Could not get context")
                }

            }


        @JvmOverloads fun reportConditionOutcome(decision: Boolean, context: Context = appContext) {
            val serviceIntent = Intent(context, TracingService::class.java)
            serviceIntent.action = TracingService.ACTION_ENQUEUE_TRACE_ITEM
            serviceIntent.putExtra(TracingService.EXTRA_ITEM_TYPE,
                    TracingService.ITEM_TYPE_PATH_TRACKING)
            serviceIntent.putExtra(TracingService.EXTRA_TRACE_ITEM, PathTrackingTraceItem(getLastExecutedStatement(), decision) as Parcelable)
            context.startService(serviceIntent)
        }


        @JvmOverloads fun reportConditionOutcomeSynchronous(decision: Boolean,
                                                            context: Context = appContext) {
            // Create the trace item to be enqueued
            val traceItem = PathTrackingTraceItem(
                    getLastExecutedStatement(), decision)
            sendTraceItemSynchronous(context, traceItem)
        }


        private fun flushBootupQueue() {
            if (tracingService == null || bootupQueue.isEmpty())
                return

            synchronized(bootupQueue) {
                if (bootupQueue.isEmpty())
                    return

                // Flush it
                while (!bootupQueue.isEmpty()) {
                    val ti = bootupQueue.poll()
                    if (ti != null)
                        tracingService!!.enqueueTraceItem(ti)
                }
            }
        }


        @JvmOverloads fun dumpTracingData(context: Context = appContext) {
            Log.i(SharedClassesSettings.TAG, "Sending an intent to dump tracing data...")
            val serviceIntent = Intent(context, TracingService::class.java)
            serviceIntent.action = TracingService.ACTION_DUMP_QUEUE
            context.startService(serviceIntent)
            Log.i(SharedClassesSettings.TAG, "Tracing data dumped via intent")
        }


        @JvmOverloads fun dumpTracingDataSynchronous(context: Context = appContext) {
            // If we don't have a service connection yet, we must directly send the
            // contents of our boot-up queue
            if (tracingService == null && !bootupQueue.isEmpty()) {
                Log.i(SharedClassesSettings.TAG, String.format("Flushing " + "boot-up queue of %d elements...", bootupQueue.size))
                val communicator = ServerCommunicator(bootupQueue)
                val items = ArrayList<IClientRequest>(bootupQueue.size)
                while (!bootupQueue.isEmpty()) {
                    val ti = bootupQueue.poll() ?: break
                    items.add(ti)
                }
                communicator.send(items, true)
                Log.i(SharedClassesSettings.TAG, "All elements in queue sent.")
                return
            } else {
                // If we have a service connection, we must make sure to flush the
                // trace items we accumulated during boot-up
                flushBootupQueue()
            }

            try {
                Log.i(SharedClassesSettings.TAG, "Dumping trace queue on binder...")
                tracingService!!.dumpQueue()
                Log.i(SharedClassesSettings.TAG, "Done.")
            } catch (ex: RuntimeException) {
                Log.e(SharedClassesSettings.TAG, "Binder communication failed: " + ex.message)
            }

        }


        @JvmOverloads fun reportMethodCallSynchronous(codePosition: Int, context: Context = appContext) {
            sendTraceItemSynchronous(context, MethodCallItem(codePosition))
        }


        @JvmOverloads fun reportMethodReturnSynchronous(codePosition: Int, context: Context = appContext) {
            sendTraceItemSynchronous(context, MethodReturnItem(codePosition))
        }


        @JvmOverloads fun reportMethodEnterSynchronous(codePosition: Int, context: Context = appContext) {
            sendTraceItemSynchronous(context, MethodEnterItem(codePosition))
        }


        @JvmOverloads fun reportMethodLeaveSynchronous(codePosition: Int, context: Context = appContext) {
            sendTraceItemSynchronous(context, MethodLeaveItem(codePosition))
        }


        @JvmOverloads fun reportTargetReachedSynchronous(context: Context = appContext) {
            Log.i(SharedClassesSettings.TAG, "Target location has been reached.")

            sendTraceItemSynchronous(context, TargetReachedTraceItem(
                    getLastExecutedStatement()))

            // This is usually the end of the analysis, so make sure to get our
            // data out
            dumpTracingDataSynchronous(context)
        }


        @JvmOverloads fun sendDexFileToServer(dexFileName: String, dexFile: ByteArray, context: Context = appContext) {
            // Since dex files can be large and we need to make sure that they are
            // sent even if the app crashes afterwards, we write them to disk. The
            // separate watchdog app will pick them up there.
            val ti = DexFileTransferTraceItem(dexFileName, dexFile,
                    getLastExecutedStatement(), globalLastExecutedStatement)
            Log.i(SharedClassesSettings.TAG, "Writing dex file of " + dexFile.size
                    + " bytes at location " + getLastExecutedStatement()
                    + " (" + ti.lastExecutedStatement + " in object)")
            var fos: FileOutputStream? = null
            var oos: ObjectOutputStream? = null
            try {
                // Create the target directory
                val storageDir = FileBasedTracingUtils.fuzzerDirectory

                // Serialize the object
                val targetFile: File
                try {
                    targetFile = File.createTempFile("evofuzz", ".dat", storageDir)
                    fos = FileOutputStream(targetFile)
                    oos = ObjectOutputStream(fos)
                    oos.writeObject(ti)
                    Log.i(SharedClassesSettings.TAG, "Dex file written to disk for watchdog")
                } catch (e: IOException) {
                    // ignore it, we can't really do much about it
                    Log.e(SharedClassesSettings.TAG, "Could not write serialized trace item to disk: " + e.message)
                }

            } finally {
                if (oos != null)
                    try {
                        oos.close()
                    } catch (e1: IOException) {
                        // ignore it, there's little we can do
                        Log.e(SharedClassesSettings.TAG, "Could not close object stream")
                    }

                if (fos != null)
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        // ignore it, there's little we can do
                        Log.e(SharedClassesSettings.TAG, "Could not close file stream")
                    }

            }
        }


        fun reportDynamicValue(dynamicValue: String, paramIdx: Int) {
            reportDynamicValue(appContext, dynamicValue, paramIdx)
        }


        fun reportDynamicValue(context: Context, dynamicValue: String?,
                               paramIdx: Int) {
            if (dynamicValue != null && dynamicValue.length > 0) {
                sendTraceItemSynchronous(context, DynamicStringValueTraceItem(
                        dynamicValue, paramIdx, getLastExecutedStatement()))
            }
        }


        fun reportDynamicValue(dynamicValue: Int, paramIdx: Int) {
            reportDynamicValue(appContext, dynamicValue, paramIdx)
        }


        fun reportDynamicValue(context: Context, dynamicValue: Int,
                               paramIdx: Int) {
            sendTraceItemSynchronous(context, DynamicIntValueTraceItem(
                    dynamicValue, paramIdx, getLastExecutedStatement()))
        }


        fun reportTimingBomb(originalValue: Long, newValue: Long) {
            reportTimingBomb(appContext, originalValue, newValue)
        }


        fun reportTimingBomb(context: Context, originalValue: Long, newValue: Long) {
            sendTraceItemSynchronous(context, TimingBombTraceItem(originalValue, newValue))
        }


        fun sendTraceItemSynchronous(context: Context,
                                     traceItem: TraceItem) {
            // If we don't have a service connection yet, we use our own boot-up
            // queue
            if (tracingService == null) {
                bootupQueue.add(traceItem)
                return
            } else {
                // If we have a service connection, we must make sure to flush the
                // trace items we accumulated during boot-up
                flushBootupQueue()
            }

            try {
                tracingService!!.enqueueTraceItem(traceItem)
            } catch (ex: RuntimeException) {
                Log.e(SharedClassesSettings.TAG, "Binder communication failed: " + ex.message)
            }

        }
    }

}
