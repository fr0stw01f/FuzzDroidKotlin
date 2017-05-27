package de.tu_darmstadt.sse.additionalappclasses.hooking

import java.util.regex.Matcher
import java.util.regex.Pattern


class FieldHookInfo(private val fieldSignature: String) : HookInfo {
    val className: String
    val fieldName: String

    var afterHook: AbstractFieldHookAfter? = null
        private set

    init {
        this.className = getClassNameFromSignature()
        this.fieldName = getFieldNameFromSignature()
    }

    private fun getClassNameFromSignature(): String {
        val pattern = "<(.*):.*>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(fieldSignature)
        if (m.find()) {
            return m.group(1)
        } else {
            throw RuntimeException("wrong format for className")
        }
    }

    private fun getFieldNameFromSignature(): String
    {
        val pattern = "<.*\\s(.*)>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(fieldSignature)
        if (m.find()) {
            return m.group(1)
        } else {
            throw RuntimeException("wrong format for className")
        }
    }

    fun persistentHookAfter(fieldValue: Any) {
        this.afterHook = PersistentFieldHookAfter(fieldValue)
    }
}
