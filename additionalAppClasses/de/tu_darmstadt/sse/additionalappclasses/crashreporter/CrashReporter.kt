package de.tu_darmstadt.sse.additionalappclasses.crashreporter

import java.lang.Thread.UncaughtExceptionHandler
import java.util.Collections

import android.util.Log
import de.tu_darmstadt.sse.additionalappclasses.tracing.BytecodeLogger
import de.tu_darmstadt.sse.sharedclasses.SharedClassesSettings
import de.tu_darmstadt.sse.sharedclasses.crashreporter.CrashReportItem
import de.tu_darmstadt.sse.sharedclasses.networkconnection.IClientRequest
import de.tu_darmstadt.sse.sharedclasses.networkconnection.ServerCommunicator


object CrashReporter {

    private val uch = object : UncaughtExceptionHandler {

        private val communicator = ServerCommunicator(this)

        override fun uncaughtException(arg0: Thread, arg1: Throwable) {
            Log.i(SharedClassesSettings.TAG, "Crash reporter started: " + arg1.toString()
                    + " at " + arg1.stackTrace)
            if (arg1.cause != null)
                Log.i(SharedClassesSettings.TAG, "Cause: " + arg1.cause.toString())
            if (arg1.cause?.cause != null)
                Log.i(SharedClassesSettings.TAG, "Cause 2: " + arg1.cause?.cause.toString())
            if (arg1.cause?.cause?.cause != null)
                Log.i(SharedClassesSettings.TAG, "Cause 3: " + arg1.cause?.cause?.cause.toString())

            // Make sure that we flush the trace items before we die
            BytecodeLogger.dumpTracingDataSynchronous()

            // Send the crash report
            val ci = CrashReportItem(arg1.message!!,
                    BytecodeLogger.getLastExecutedStatement())
            communicator.send(setOf<IClientRequest>(ci), true)
        }

    }


    fun registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(uch)
    }

}
