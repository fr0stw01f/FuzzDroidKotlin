package de.tu_darmstadt.sse.sharedclasses.tracing

import android.os.Parcel
import android.os.Parcelable


class DynamicStringValueTraceItem : DynamicValueTraceItem {

    var stringValue: String? = null
        private set

    private constructor() : super()

    constructor(stringValue: String, paramIdx: Int,
                lastExecutedStatement: Int) : super(paramIdx, lastExecutedStatement) {
        this.stringValue = stringValue
    }

    override fun writeToParcel(parcel: Parcel, arg1: Int) {
        super.writeToParcel(parcel, arg1)
        parcel.writeString(stringValue)
    }

    override fun readFromParcel(parcel: Parcel) {
        super.readFromParcel(parcel)
        this.stringValue = parcel.readString()
    }

    companion object {
        private val serialVersionUID = 7558624604497311423L

        val CREATOR: Parcelable.Creator<DynamicStringValueTraceItem> = object : Parcelable.Creator<DynamicStringValueTraceItem> {

            override fun createFromParcel(parcel: Parcel): DynamicStringValueTraceItem {
                val ti = DynamicStringValueTraceItem()
                ti.readFromParcel(parcel)
                return ti
            }

            override fun newArray(size: Int): Array<DynamicStringValueTraceItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}
