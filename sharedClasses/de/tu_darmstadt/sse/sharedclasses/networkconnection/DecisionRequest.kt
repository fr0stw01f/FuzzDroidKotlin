package de.tu_darmstadt.sse.sharedclasses.networkconnection

import java.util.Arrays


class DecisionRequest : IClientRequest, Cloneable {

    val codePosition: Int

    val loggingPointSignature: String?

    val isHookAfter: Boolean

    var runtimeValueOfReturn: Any? = null

    var runtimeValuesOfParams: Array<Any>? = null

    constructor(codePosition: Int, loggingPointSignature: String, hookAfter: Boolean) {
        this.codePosition = codePosition
        this.loggingPointSignature = loggingPointSignature
        this.isHookAfter = hookAfter
    }

    private constructor(original: DecisionRequest) {
        this.codePosition = original.codePosition
        this.loggingPointSignature = original.loggingPointSignature
        this.isHookAfter = original.isHookAfter
        this.runtimeValueOfReturn = original.runtimeValueOfReturn
        this.runtimeValuesOfParams = original.runtimeValuesOfParams
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(String.format("[HOOK]%s\n", this.loggingPointSignature))
        sb.append(String.format("\t codeposition=%d\n", this.codePosition))
        sb.append(String.format("\t hookAfter=%s\n", if (this.isHookAfter == true) "true" else "false"))
        if (isHookAfter)
            sb.append(String.format("\t runtime value of return value: %s\n", this.runtimeValueOfReturn))
        else {
            var params = "["
            for (i in this.runtimeValuesOfParams!!.indices) {
                if (i != this.runtimeValuesOfParams!!.size - 1)
                    params += this.runtimeValuesOfParams!![i].toString() + ", "
                else
                    params += this.runtimeValuesOfParams!![i]
            }
            params += "]"
            sb.append(String.format("\t runtime values of params: %s\n", params))
        }
        return sb.toString()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as DecisionRequest?
        if (codePosition != other!!.codePosition)
            return false
        if (isHookAfter != other.isHookAfter)
            return false
        if (loggingPointSignature == null) {
            if (other.loggingPointSignature != null)
                return false
        } else if (loggingPointSignature != other.loggingPointSignature)
            return false
        if (runtimeValueOfReturn == null) {
            if (other.runtimeValueOfReturn != null)
                return false
        } else if (runtimeValueOfReturn != other.runtimeValueOfReturn)
            return false
        if (!Arrays.equals(runtimeValuesOfParams, other.runtimeValuesOfParams))
            return false
        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + codePosition
        result = prime * result + if (isHookAfter) 1231 else 1237
        result = prime * result + (loggingPointSignature?.hashCode() ?: 0)
        result = prime * result + (runtimeValueOfReturn?.hashCode() ?: 0)
        result = prime * result + Arrays.hashCode(runtimeValuesOfParams)
        return result
    }

    public override fun clone(): DecisionRequest {
        return DecisionRequest(this)
    }

    companion object {
        private val serialVersionUID = 5206760883356756113L
    }

}
