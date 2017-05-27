package de.tu_darmstadt.sse.additionalappclasses.hooking

import de.tu_darmstadt.sse.additionalappclasses.util.UtilHook
import de.tu_darmstadt.sse.sharedclasses.networkconnection.DecisionRequest
import de.tu_darmstadt.sse.sharedclasses.networkconnection.NetworkConnectionInitiator
import de.tu_darmstadt.sse.sharedclasses.networkconnection.ServerCommunicator
import de.tu_darmstadt.sse.sharedclasses.networkconnection.ServerResponse


class AnalysisDependentFieldHookAfter(private val fieldSignature: String) : AbstractFieldHookAfter() {

    private var newValueAvailable: Boolean? = null
    private var newValue: Any? = null

    fun retrieveValueFromServer(runtimeValue: Any) {
        val sc = NetworkConnectionInitiator.serverCommunicator
        val lastCodePosition = getLastCodePosition()
        val cRequest = DecisionRequest(lastCodePosition, fieldSignature, true)
        val cleanObject = UtilHook.prepareValueForExchange(runtimeValue)
        cRequest.runtimeValueOfReturn = cleanObject
        val response = sc!!.getResultForRequest(cRequest)
        newValueAvailable = response.doesResponseExist()
        if (newValueAvailable!!)
            newValue = response.returnValue
    }

    override fun isValueReplacementNecessary(): Boolean? {
        return newValueAvailable
    }

    override fun getNewValue(): Any? {
        return newValue
    }

}
