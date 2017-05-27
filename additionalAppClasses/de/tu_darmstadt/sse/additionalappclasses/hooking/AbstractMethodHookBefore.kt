package de.tu_darmstadt.sse.additionalappclasses.hooking

import de.tu_darmstadt.sse.sharedclasses.util.Pair


abstract class AbstractMethodHookBefore : AbstractMethodHook() {

    abstract fun getParamValuesToReplace(): Set<Pair<Int, Any>>?
}
