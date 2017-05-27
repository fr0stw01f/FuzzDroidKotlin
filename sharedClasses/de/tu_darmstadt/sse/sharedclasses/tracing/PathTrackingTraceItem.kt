package de.tu_darmstadt.sse.sharedclasses.tracing

import android.os.Parcel
import android.os.Parcelable


class PathTrackingTraceItem : TraceItem {

    var lastConditionalResult: Boolean = false
        private set

    private constructor() : super()

    constructor(lastExecutedStatement: Int,
                lastConditionResult: Boolean) : super(lastExecutedStatement) {
        this.lastConditionalResult = lastConditionResult
    }

    override fun writeToParcel(parcel: Parcel, arg1: Int) {
        super.writeToParcel(parcel, arg1)
        parcel.writeByte((if (lastConditionalResult) 0 else 1).toByte())
    }

    override fun readFromParcel(parcel: Parcel) {
        super.readFromParcel(parcel)
        this.lastConditionalResult = parcel.readByte().toInt() == 1
    }

    companion object {

        private val serialVersionUID = -8948293905139569335L

        val CREATOR: Parcelable.Creator<PathTrackingTraceItem> = object : Parcelable.Creator<PathTrackingTraceItem> {

            override fun createFromParcel(parcel: Parcel): PathTrackingTraceItem {
                val ti = PathTrackingTraceItem()
                ti.readFromParcel(parcel)
                return ti
            }

            override fun newArray(size: Int): Array<PathTrackingTraceItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}
