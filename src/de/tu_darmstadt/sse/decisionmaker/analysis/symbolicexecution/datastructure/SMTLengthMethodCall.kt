package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTLengthMethodCall(private val stringValue: SMTValue) : StringMethodCall() {

    override fun toString(): String {
        return String.format("( Length %s )", stringValue.toString())
    }
}
