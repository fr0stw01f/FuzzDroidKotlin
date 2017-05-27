package de.tu_darmstadt.sse.additionalappclasses.hooking

import android.util.Log
import de.tu_darmstadt.sse.sharedclasses.SharedClassesSettings
import de.tu_darmstadt.sse.sharedclasses.networkconnection.DecisionRequest
import de.tu_darmstadt.sse.sharedclasses.networkconnection.NetworkConnectionInitiator
import de.tu_darmstadt.sse.sharedclasses.networkconnection.ServerCommunicator
import de.tu_darmstadt.sse.sharedclasses.networkconnection.ServerResponse


class SimpleBooleanHookAfter(private val methodSignature: String) : AbstractMethodHookAfter() {

    private var runtimeValueOfReturnAfterHooking: Any? = null

    private var runtimeValueOfReturnAvailable: Boolean = false

    fun retrieveBooleanValueFromServer() {
        val sc = NetworkConnectionInitiator.serverCommunicator
        val lastCodePosition = getLastCodePosition()
        val cRequest = DecisionRequest(lastCodePosition, methodSignature, true)

        val response = sc!!.getResultForRequest(cRequest)

        if (response == null) {
            Log.e(SharedClassesSettings.TAG, "NULL response received from server")
            runtimeValueOfReturnAvailable = false
            runtimeValueOfReturnAfterHooking = null
            return
        }

        Log.i(SharedClassesSettings.TAG, "Retrieved boolean decision from server")
        runtimeValueOfReturnAvailable = response.doesResponseExist()

        if (runtimeValueOfReturnAvailable) {
            runtimeValueOfReturnAfterHooking = response.returnValue
            Log.d(SharedClassesSettings.TAG, "Return value from server: " + runtimeValueOfReturnAfterHooking!!)
        } else
            Log.d(SharedClassesSettings.TAG, "Server had no response value for us")
    }

    override fun getReturnValue(): Any? {
        return runtimeValueOfReturnAfterHooking
    }

    override fun isValueReplacementNecessary(): Boolean {
        return runtimeValueOfReturnAvailable
    }

}
