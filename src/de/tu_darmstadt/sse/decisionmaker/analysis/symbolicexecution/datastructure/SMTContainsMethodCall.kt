package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTContainsMethodCall(private val stringValue: SMTValue, private val containsValue: SMTValue) : StringMethodCall() {

    override fun toString(): String {
        return String.format("( Contains %s %s )", stringValue, containsValue)
    }
}
