package de.tu_darmstadt.sse.sharedclasses.tracing

import android.os.Parcel
import android.os.Parcelable


class TargetReachedTraceItem : TraceItem {

    private constructor() : super()

    constructor(lastExecutedStatement: Int) : super(lastExecutedStatement)

    companion object {

        private val serialVersionUID = 2184482709277468438L

        val CREATOR: Parcelable.Creator<TargetReachedTraceItem> = object : Parcelable.Creator<TargetReachedTraceItem> {

            override fun createFromParcel(parcel: Parcel): TargetReachedTraceItem {
                val ti = TargetReachedTraceItem()
                ti.readFromParcel(parcel)
                return ti
            }

            override fun newArray(size: Int): Array<TargetReachedTraceItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}
