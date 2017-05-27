package de.tu_darmstadt.sse.sharedclasses.tracing

import android.os.Parcel
import android.os.Parcelable


class DynamicIntValueTraceItem : DynamicValueTraceItem {

    var intValue: Int = 0
        private set

    protected constructor() : super()

    constructor(intValue: Int, paramIdx: Int,
                lastExecutedStatement: Int) : super(paramIdx, lastExecutedStatement) {
        this.intValue = intValue
    }

    override fun writeToParcel(parcel: Parcel, arg1: Int) {
        super.writeToParcel(parcel, arg1)
        parcel.writeInt(intValue)
    }

    override fun readFromParcel(parcel: Parcel) {
        super.readFromParcel(parcel)
        this.intValue = parcel.readInt()
    }

    companion object {


        private val serialVersionUID = -7361222929840012462L

        val CREATOR: Parcelable.Creator<DynamicIntValueTraceItem> = object : Parcelable.Creator<DynamicIntValueTraceItem> {

            override fun createFromParcel(parcel: Parcel): DynamicIntValueTraceItem {
                val ti = DynamicIntValueTraceItem()
                ti.readFromParcel(parcel)
                return ti
            }

            override fun newArray(size: Int): Array<DynamicIntValueTraceItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}
