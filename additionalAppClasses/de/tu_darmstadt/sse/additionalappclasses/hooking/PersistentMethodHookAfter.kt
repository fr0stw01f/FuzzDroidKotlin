package de.tu_darmstadt.sse.additionalappclasses.hooking


class PersistentMethodHookAfter(private val returnValue: Any) : AbstractMethodHookAfter() {

    override fun getReturnValue(): Any {
        return returnValue
    }

    override fun isValueReplacementNecessary(): Boolean {
        return true
    }

}
