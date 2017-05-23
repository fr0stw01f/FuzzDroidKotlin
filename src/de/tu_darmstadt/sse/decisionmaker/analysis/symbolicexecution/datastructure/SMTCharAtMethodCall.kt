package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTCharAtMethodCall(private val stringValue: SMTValue, private val indexOfChar: SMTValue) : StringMethodCall() {

    override fun toString(): String {
        return String.format("( CharAt %s %s )", stringValue, indexOfChar)
    }
}
