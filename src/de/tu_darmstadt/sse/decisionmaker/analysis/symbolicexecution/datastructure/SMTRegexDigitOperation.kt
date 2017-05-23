package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

class SMTRegexDigitOperation(private val stringValue: SMTBinding) : SMTStatement {

    override fun getStatement(): SMTStatement {
        return this
    }

    override fun toString(): String {
        return String.format("( RegexIn %s ( RegexStar ( RegexDigit \"\" ) ) )", stringValue.binding)
    }

}
