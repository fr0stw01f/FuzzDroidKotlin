package de.tu_darmstadt.sse.additionalappclasses.hooking

import java.io.Serializable


class DummyValue : Serializable {

    override fun toString(): String {
        return "<DUMMY>"
    }

    companion object {
        private const val serialVersionUID = -3619572732272288459L
    }
}
