package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTEndsWithMethodCall(private val stringValue: SMTValue, private val stringEndsWith: SMTValue) : StringMethodCall() {

    override fun toString(): String {
        return String.format("( EndsWith %s %s )", stringValue, stringEndsWith)
    }
}
