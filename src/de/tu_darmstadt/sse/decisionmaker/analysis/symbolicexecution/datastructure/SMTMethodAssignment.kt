package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure


class SMTMethodAssignment(lhs: SMTBinding, private val rhs: SMTMethodCall) : SMTAssignment(lhs) {

    override fun getStatement(): SMTStatement {
        return this
    }

    override fun toString(): String {
        return String.format("(= %s %s)", lhs.binding, rhs)
    }

}
