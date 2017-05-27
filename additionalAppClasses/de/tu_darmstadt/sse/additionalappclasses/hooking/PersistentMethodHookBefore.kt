package de.tu_darmstadt.sse.additionalappclasses.hooking

import de.tu_darmstadt.sse.sharedclasses.util.Pair


class PersistentMethodHookBefore(private val paramValuePair: Set<Pair<Int, Any>>) : AbstractMethodHookBefore() {

    override fun getParamValuesToReplace(): Set<Pair<Int, Any>> {
        return paramValuePair
    }

    override fun isValueReplacementNecessary(): Boolean {
        return true
    }

}
