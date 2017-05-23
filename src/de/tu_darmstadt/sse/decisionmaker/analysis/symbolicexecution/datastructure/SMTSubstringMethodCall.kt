package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTSubstringMethodCall(private val stringValue: SMTValue, private val from: SMTValue, private val to: SMTValue) : StringMethodCall() {

    override fun toString(): String {
        return String.format("( Substring %s %s %s )", stringValue.toString(), from.toString(), to.toString())
    }
}
