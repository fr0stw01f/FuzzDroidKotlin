package de.tu_darmstadt.sse.appinstrumentation.transformer

import soot.tagkit.AttributeValueException
import soot.tagkit.Tag


class InstrumentedCodeTag : Tag {
    override fun getName(): String {
        return name
    }

    @Throws(AttributeValueException::class)
    override fun getValue(): ByteArray? {
        return null
    }

    companion object {

        val name = "InstrumentedCodeTag"
    }

}
