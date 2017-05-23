package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTBooleanEqualsAssignment(outerLHS: SMTBinding, private val innerLHS: SMTValue, private val innerRHS: SMTValue) : SMTAssignment(outerLHS) {

    override fun toString(): String {
        return String.format("(= %s ( = %s %s ) )", lhs.binding, innerLHS, innerRHS)
    }

    override fun getStatement(): SMTStatement {
        return this
    }
}
