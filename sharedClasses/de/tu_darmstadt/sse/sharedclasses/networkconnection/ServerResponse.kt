package de.tu_darmstadt.sse.sharedclasses.networkconnection

import java.io.Serializable
import java.util.HashSet

import de.tu_darmstadt.sse.sharedclasses.util.Pair


class ServerResponse : Serializable, Cloneable {

    private var responseExist: Boolean = false

    var returnValue: Any? = null

    var paramValues: Set<Pair<Int, Any>>? = null

    var analysisName: String? = null


    constructor()


    private constructor(original: ServerResponse) {
        this.responseExist = original.responseExist
        this.returnValue = original.returnValue
        this.paramValues = if (original.paramValues == null) null else HashSet(original.paramValues!!)
    }

    fun setResponseExist(responseExist: Boolean) {
        this.responseExist = responseExist
    }

    fun doesResponseExist(): Boolean {
        return responseExist
    }

    override fun toString(): String {
        if (!responseExist)
            return "[NO VALUE AVAILABLE]"
        else {
            var response = "[NEW VALUE] "
            if (returnValue != null)
                response += returnValue
            else {
                if (paramValues != null) {
                    for (pair in paramValues!!)
                        response += String.format("\n\t param %d = %s", pair.getFirst(), pair.getSecond())
                } else
                    throw RuntimeException("the response has to contain either a value for the parameter or return value")
            }
            response += String.format(" [ANALYSIS_NAME] %s", this.analysisName)
            return response
        }
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (paramValues == null) 0 else paramValues!!.hashCode()
        result = prime * result + if (responseExist) 1231 else 1237
        result = prime * result + if (returnValue == null) 0 else returnValue!!.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as ServerResponse?
        if (paramValues == null) {
            if (other!!.paramValues != null)
                return false
        } else if (paramValues != other!!.paramValues)
            return false
        if (responseExist != other.responseExist)
            return false
        if (returnValue == null) {
            if (other.returnValue != null)
                return false
        } else if (returnValue != other.returnValue)
            return false
        return true
    }

    public override fun clone(): ServerResponse {
        return ServerResponse(this)
    }

    companion object {
        private const val serialVersionUID = 5488569934511264853L


        val emptyResponse: ServerResponse
            get() {
                val response = ServerResponse()
                response.responseExist = false
                return response
            }
    }

}
