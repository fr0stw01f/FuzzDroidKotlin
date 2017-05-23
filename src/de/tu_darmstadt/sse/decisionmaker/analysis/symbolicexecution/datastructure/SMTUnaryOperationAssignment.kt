package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTUnaryOperationAssignment(lhs: SMTBinding, private val rhs: SMTUnaryOperation) : SMTAssignment(lhs) {

    override fun toString(): String {
        return String.format("(= %s %s )", lhs.binding, rhs)
    }


    override fun getStatement(): SMTStatement {
        return this
    }
}
