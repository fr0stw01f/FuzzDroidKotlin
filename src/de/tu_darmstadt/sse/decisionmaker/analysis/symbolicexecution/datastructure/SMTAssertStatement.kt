package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure


class SMTAssertStatement(val statement: SMTStatement) {

    override fun toString(): String {
        return String.format("( assert %s )", statement.toString())
    }
}
