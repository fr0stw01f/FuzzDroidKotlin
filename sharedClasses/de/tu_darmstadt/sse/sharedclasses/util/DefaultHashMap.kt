package de.tu_darmstadt.sse.sharedclasses.util

import java.util.HashMap

class DefaultHashMap<K, V>(protected var defaultValue: V) : HashMap<K, V>() {

    override fun get(key: K): V? {
        return if (super.containsKey(key)) super.get(key) else defaultValue
    }

    //override fun get(k: Any): V? {
    //    return if (super.containsKey(k)) super.get(k)!! else defaultValue
    //}

    companion object {
        private val serialVersionUID = -1099648480486824057L
    }
}