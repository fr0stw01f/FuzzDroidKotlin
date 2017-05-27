package de.tu_darmstadt.sse.sharedclasses.networkconnection.serializables

import java.io.Serializable

import de.tu_darmstadt.sse.sharedclasses.networkconnection.IClientRequest


class BinarySerializableObject(val binaryData: ByteArray) : Serializable, IClientRequest {
    companion object {
        private const val serialVersionUID = -1043817079853486666L
    }

}
