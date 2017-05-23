package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTConstantValue<T>(private val constantValue: T) : SMTValue {

    override fun toString(): String {
        val returnValue: String?
        if (constantValue is String)
            returnValue = String.format("\"%s\"", constantValue.toString())
        else
            returnValue = constantValue.toString()
        return returnValue
    }
}
