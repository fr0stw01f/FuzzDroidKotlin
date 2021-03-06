package de.tu_darmstadt.sse.apkspecific.CodeModel


class CodePosition(id: Int, enclosingMethod: String, lineNumber: Int,
                   sourceLineNumber: Int) {

    var id = -1
    var enclosingMethod: String? = null
    var lineNumber = -1
    var sourceLineNumber = -1

    init {
        this.id = id
        this.enclosingMethod = enclosingMethod
        this.lineNumber = lineNumber
        this.sourceLineNumber = sourceLineNumber
    }

}
