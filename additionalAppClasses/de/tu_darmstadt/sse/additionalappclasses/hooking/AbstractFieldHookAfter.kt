package de.tu_darmstadt.sse.additionalappclasses.hooking

import de.tu_darmstadt.sse.additionalappclasses.tracing.BytecodeLogger


abstract class AbstractFieldHookAfter {

    protected fun getLastCodePosition(): Int {
        return BytecodeLogger.getLastExecutedStatement()
    }

    abstract fun isValueReplacementNecessary(): Boolean?

    abstract fun getNewValue(): Any?
}
