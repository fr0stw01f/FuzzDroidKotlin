package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure


class SMTSimpleAssignment(lhs: SMTBinding, private val rhs: SMTValue) : SMTAssignment(lhs) {

    override fun getStatement(): SMTStatement {
        return this
    }

    override fun toString(): String {
        return String.format("(= %s %s)", lhs.binding, rhs)
    }

}
