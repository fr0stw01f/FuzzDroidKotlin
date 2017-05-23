package de.tu_darmstadt.sse.decisionmaker.analysis.symbolicexecution.datastructure


class SMTBinding {
    val variableName: String
    val type: TYPE
    var version: Int = 0

    enum class TYPE {
        String, Int, Bool, Real
    }

    constructor(variableName: String, type: SMTBinding.TYPE, version: Int) {
        this.variableName = variableName
        this.type = type
        this.version = version
    }

    constructor(variableName: String, type: SMTBinding.TYPE) {
        this.variableName = variableName
        this.type = type
        this.version = 0
    }

    val binding: String
        get() = String.format("%s_%d", variableName, version)

    override fun toString(): String {
        return String.format("%s %s_%d", type.name, variableName, version)
    }
}
