package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure


class SMTUnaryOperation(private val operator: SMTUnaryOperation.SMTUnaryOperator, private val lhs: SMTValue, private val rhs: SMTValue) {
    enum class SMTUnaryOperator {
        Plus, Minus, Times, Divided, Modulo
    }

    override fun toString(): String {
        var stringOperator: String? = null
        when (operator) {
            SMTUnaryOperation.SMTUnaryOperator.Plus -> stringOperator = "+"
            SMTUnaryOperation.SMTUnaryOperator.Minus -> stringOperator = "-"
            SMTUnaryOperation.SMTUnaryOperator.Times -> stringOperator = "*"
            SMTUnaryOperation.SMTUnaryOperator.Divided -> stringOperator = "/"
            SMTUnaryOperation.SMTUnaryOperator.Modulo -> stringOperator = "%"
        }
        return String.format("(%s %s %s)", stringOperator, lhs, rhs)
    }
}
