package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure

import java.util.ArrayList


class SMTProgram : Cloneable {
    //SSA-based variable declaration
    internal var variableDeclarations: MutableList<SMTBinding>
    //all assert statements
    internal var assertStatements: MutableList<SMTAssertStatement>

    constructor() {
        variableDeclarations = ArrayList<SMTBinding>()
        assertStatements = ArrayList<SMTAssertStatement>()
    }

    constructor(variableDeclarations: MutableList<SMTBinding>, assertStatements: MutableList<SMTAssertStatement>) {
        this.variableDeclarations = variableDeclarations
        this.assertStatements = assertStatements
    }

    fun addVariableDeclaration(variableDecl: SMTBinding?) {
        if (variableDecl == null)
            throw RuntimeException("binding should not be null")
        this.variableDeclarations.add(variableDecl)
    }

    fun addAssertStatement(assertStmt: SMTAssertStatement?) {
        if (assertStmt == null)
            throw RuntimeException("assertion should not be null")
        this.assertStatements.add(assertStmt)
    }

    fun removeAssertStatement(assertStmt: SMTAssertStatement) {
        if (assertStatements.contains(assertStmt))
            assertStatements.remove(assertStmt)
    }


    fun getVariableDeclarations(): List<SMTBinding> {
        return variableDeclarations
    }

    fun getAssertStatements(): List<SMTAssertStatement> {
        return assertStatements
    }

    override fun toString(): String {
        val sb = StringBuilder()
        variableDeclarations
                .map { String.format("(declare-variable %s %s)\n", it.binding, it.type.toString()) }
                .forEach { sb.append(it) }
        sb.append("\n")
        for (assertStmt in assertStatements) {
            sb.append(assertStmt.toString() + "\n")
        }
        sb.append("\n")
        sb.append("(check-sat)\n")
        sb.append("(get-model)")
        return sb.toString()
    }

    public override fun clone(): SMTProgram {
        //Deep copy
        //note that it is not necessary to make deep copy of the SMTBinding elements
        val newVariableDeclarations = ArrayList(variableDeclarations)
        //note that it is not necessary to make deep copy of the SMTAssertStatement elements
        val newAssertStmts = ArrayList(assertStatements)

        val newSMTProgram = SMTProgram(newVariableDeclarations, newAssertStmts)
        return newSMTProgram
    }
}
