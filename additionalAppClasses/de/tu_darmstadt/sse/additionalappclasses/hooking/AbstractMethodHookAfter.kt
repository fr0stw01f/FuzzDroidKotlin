package de.tu_darmstadt.sse.additionalappclasses.hooking


abstract class AbstractMethodHookAfter : AbstractMethodHook() {

    abstract fun getReturnValue(): Any?
}
