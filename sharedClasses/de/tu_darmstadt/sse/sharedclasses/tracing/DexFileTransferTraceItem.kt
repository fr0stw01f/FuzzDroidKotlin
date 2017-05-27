package de.tu_darmstadt.sse.sharedclasses.tracing

import android.os.Parcel
import android.os.Parcelable


class DexFileTransferTraceItem : TraceItem {

    var fileName: String? = null
        private set
    var dexFile: ByteArray? = null
        private set

    private constructor() : super()

    constructor(fileName: String, dexFile: ByteArray,
                lastExecutedStatement: Int, globalLastExecutedStatement: Int) : super(lastExecutedStatement, globalLastExecutedStatement) {
        this.fileName = fileName
        this.dexFile = dexFile
    }

    override fun writeToParcel(parcel: Parcel, arg1: Int) {
        super.writeToParcel(parcel, arg1)
        parcel.writeString(fileName)

        parcel.writeInt(dexFile!!.size)
        parcel.writeByteArray(dexFile)
    }

    override fun readFromParcel(parcel: Parcel) {
        super.readFromParcel(parcel)
        this.fileName = parcel.readString()

        val len = parcel.readInt()
        this.dexFile = ByteArray(len)
        parcel.readByteArray(this.dexFile!!)
    }

    companion object {
        private val serialVersionUID = -2768018408182596990L

        val CREATOR: Parcelable.Creator<DexFileTransferTraceItem> = object : Parcelable.Creator<DexFileTransferTraceItem> {

            override fun createFromParcel(parcel: Parcel): DexFileTransferTraceItem {
                val ti = DexFileTransferTraceItem()
                ti.readFromParcel(parcel)
                return ti
            }

            override fun newArray(size: Int): Array<DexFileTransferTraceItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}
