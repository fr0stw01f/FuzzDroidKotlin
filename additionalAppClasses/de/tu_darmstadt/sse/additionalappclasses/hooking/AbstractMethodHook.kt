package de.tu_darmstadt.sse.additionalappclasses.hooking

import de.tu_darmstadt.sse.additionalappclasses.tracing.BytecodeLogger


abstract class AbstractMethodHook {

    protected fun getLastCodePosition(): Int {
        return BytecodeLogger.getLastExecutedStatement()
    }

    abstract fun isValueReplacementNecessary(): Boolean?

}
