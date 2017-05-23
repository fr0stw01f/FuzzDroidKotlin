package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTIndexOfMethodCall(private val stringValue: SMTValue, private val indexOf: SMTValue) : StringMethodCall() {

    override fun toString(): String {
        return String.format("( Indexof %s %s )", stringValue.toString(), indexOf.toString())
    }
}
