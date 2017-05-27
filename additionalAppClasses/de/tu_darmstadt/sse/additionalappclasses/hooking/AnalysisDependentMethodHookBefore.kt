package de.tu_darmstadt.sse.additionalappclasses.hooking

import android.util.Log
import de.tu_darmstadt.sse.additionalappclasses.tracing.BytecodeLogger
import de.tu_darmstadt.sse.additionalappclasses.util.UtilHook
import de.tu_darmstadt.sse.sharedclasses.SharedClassesSettings
import de.tu_darmstadt.sse.sharedclasses.networkconnection.DecisionRequest
import de.tu_darmstadt.sse.sharedclasses.networkconnection.NetworkConnectionInitiator
import de.tu_darmstadt.sse.sharedclasses.networkconnection.ServerCommunicator
import de.tu_darmstadt.sse.sharedclasses.networkconnection.ServerResponse
import de.tu_darmstadt.sse.sharedclasses.util.Pair

class AnalysisDependentMethodHookBefore(private val methodSignature: String) : AbstractMethodHookBefore() {

    private var paramValuesToReplace: Set<Pair<Int, Any>>? = null
    private var needToChangeValue: Boolean? = null

    override fun isValueReplacementNecessary(): Boolean? {
        return needToChangeValue
    }

    fun retrieveValueFromServer(runtimeValues: Array<Any>) {
        // Make sure to always flush the trace before we ask for a decision
        //		BytecodeLogger.dumpTracingDataSynchronous();
        //		Log.i(SharedClassesSettings.TAG, "Flushed tracing queue to server");

        val sc = NetworkConnectionInitiator.serverCommunicator
        val lastCodePosition = getLastCodePosition()
        val cRequest = DecisionRequest(lastCodePosition, methodSignature, false)
        val preparedParameter = prepareParameterForExchange(runtimeValues)
        cRequest.runtimeValuesOfParams = preparedParameter as Array<Any>?

        val response = sc!!.getResultForRequest(cRequest)
        Log.i(SharedClassesSettings.TAG, "Retrieved decision from server")

        needToChangeValue = response.doesResponseExist()
        if (needToChangeValue!!)
            paramValuesToReplace = response.paramValues
    }

    override fun getParamValuesToReplace(): Set<Pair<Int, Any>>? {
        return paramValuesToReplace
    }

    private fun prepareParameterForExchange(params: Array<Any>): Array<Any?>? {
        val preparedParams = arrayOfNulls<Any>(params.size)
        for (i in params.indices)
            preparedParams[i] = UtilHook.prepareValueForExchange(params[i])
        return preparedParams
    }
}
