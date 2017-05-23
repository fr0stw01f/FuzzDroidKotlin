package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTBindingValue(private val value: SMTBinding) : SMTValue {

    override fun toString(): String {
        return value.binding
    }
}
