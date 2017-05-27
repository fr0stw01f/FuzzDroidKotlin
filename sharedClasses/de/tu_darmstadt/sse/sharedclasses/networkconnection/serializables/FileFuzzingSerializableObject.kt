package de.tu_darmstadt.sse.sharedclasses.networkconnection.serializables

import java.io.Serializable

import de.tu_darmstadt.sse.sharedclasses.networkconnection.FileFormat


class FileFuzzingSerializableObject(val fileFormat: FileFormat, val storageMode: Int) : Serializable {

    override fun toString(): String {
        return String.format("file format: %s | mode: %d", fileFormat, storageMode)
    }

    companion object {
        private const val serialVersionUID = 8055219086869125404L
    }
}
