package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure


class SMTComplexBinaryOperation(private val operator: SMTComplexBinaryOperation.SMTComplexBinaryOperator, private val statements: Set<SMTStatement>) : SMTStatement {
    enum class SMTComplexBinaryOperator {
        OR
    }

    override fun toString(): String {
        var stringOp: String? = null
        when (operator) {
            SMTComplexBinaryOperation.SMTComplexBinaryOperator.OR -> stringOp = "or"
        }

        val statementsAsString = StringBuilder()
        for (statement in statements)
            statementsAsString.append(statement.toString() + " ")

        return String.format("(%s %s)", stringOp, statementsAsString.toString())
    }

    override fun getStatement(): SMTStatement {
        return this
    }

}
