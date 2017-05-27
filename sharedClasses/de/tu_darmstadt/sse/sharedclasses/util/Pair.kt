package de.tu_darmstadt.sse.sharedclasses.util

import java.io.Serializable

class Pair<F, S>(private var first: F?, private var second: S?) : Serializable, Cloneable {

    protected var hashCode = 0

    fun setFirst(first: F) {
        this.first = first
        hashCode = 0
    }

    fun setSecond(second: S) {
        this.second = second
        hashCode = 0
    }

    fun setPair(no1: F, no2: S) {
        first = no1
        second = no2
        hashCode = 0
    }

    fun getFirst(): F? {
        return first
    }

    fun getSecond(): S? {
        return second
    }

    override fun hashCode(): Int {
        if (hashCode != 0)
            return hashCode

        val prime = 31
        var result = 1
        result = prime * result + if (first == null) 0 else first!!.hashCode()
        result = prime * result + if (second == null) 0 else second!!.hashCode()
        hashCode = result

        return hashCode
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as Pair<*, *>?
        if (first == null) {
            if (other!!.first != null)
                return false
        } else if (first != other!!.first)
            return false
        if (second == null) {
            if (other.second != null)
                return false
        } else if (second != other.second)
            return false
        return true
    }

    override fun toString(): String {
        return "Pair $first,$second"
    }

    companion object {
        private const val serialVersionUID = 7408444626787884925L
    }

}
