package de.tu_darmstadt.sse.additionalappclasses.hooking


class PersistentFieldHookAfter(private val newValue: Any) : AbstractFieldHookAfter() {

    override fun isValueReplacementNecessary(): Boolean {
        return true
    }

    override fun getNewValue(): Any {
        return newValue
    }
}
