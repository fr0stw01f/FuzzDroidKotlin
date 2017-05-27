package de.tu_darmstadt.sse.sharedclasses.tracing

import android.os.Parcel
import android.os.Parcelable


class TimingBombTraceItem : TraceItem {

    var originalValue: Long = 0
        private set
    var newValue: Long = 0
        private set

    private constructor()

    constructor(originalValue: Long, newValue: Long) {
        this.originalValue = originalValue
        this.newValue = newValue
    }

    override fun writeToParcel(parcel: Parcel, arg1: Int) {
        super.writeToParcel(parcel, arg1)
        parcel.writeLong(this.originalValue)
        parcel.writeLong(this.newValue)
    }

    override fun readFromParcel(parcel: Parcel) {
        super.readFromParcel(parcel)
        this.originalValue = parcel.readLong()
        this.newValue = parcel.readLong()
    }

    companion object {

        private val serialVersionUID = 1837171677027458456L

        val CREATOR: Parcelable.Creator<TimingBombTraceItem> = object : Parcelable.Creator<TimingBombTraceItem> {

            override fun createFromParcel(parcel: Parcel): TimingBombTraceItem {
                val ti = TimingBombTraceItem()
                ti.readFromParcel(parcel)
                return ti
            }

            override fun newArray(size: Int): Array<TimingBombTraceItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}
