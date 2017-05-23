package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTConcatMethodCall(private val concatLhs: SMTValue, private val concatRhs: SMTValue) : StringMethodCall() {

    override fun toString(): String {
        return String.format("( Concat %s %s )", concatLhs, concatRhs)
    }
}
