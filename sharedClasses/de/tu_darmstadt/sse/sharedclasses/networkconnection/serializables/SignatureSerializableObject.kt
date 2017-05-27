package de.tu_darmstadt.sse.sharedclasses.networkconnection.serializables

import java.io.Serializable


class SignatureSerializableObject(val encodedCertificate: ByteArray) : Serializable {
    companion object {
        private const val serialVersionUID = -1089353033691760402L
    }
}
