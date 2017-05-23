package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure


class SMTSimpleBinaryOperation(private val operator: SMTSimpleBinaryOperation.SMTSimpleBinaryOperator, private val lhs: SMTValue, private val rhs: SMTValue) : SMTStatement {
    enum class SMTSimpleBinaryOperator {
        GT, LT
    }

    override fun toString(): String {
        var operatorString: String? = null
        when (operator) {
            SMTSimpleBinaryOperation.SMTSimpleBinaryOperator.GT -> operatorString = ">"
            SMTSimpleBinaryOperation.SMTSimpleBinaryOperator.LT -> operatorString = "<"
        }

        return String.format("(%s %s %s)", operatorString, lhs, rhs)
    }

    override fun getStatement(): SMTStatement {
        return this
    }
}
