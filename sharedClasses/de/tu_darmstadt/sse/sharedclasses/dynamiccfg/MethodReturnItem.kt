package de.tu_darmstadt.sse.sharedclasses.dynamiccfg

import android.os.Parcel
import android.os.Parcelable


class MethodReturnItem : AbstractDynamicCFGItem {

    constructor() : super()

    constructor(lastExecutedStatement: Int) : super(lastExecutedStatement)

    override fun toString(): String {
        return "Method return: " + lastExecutedStatement
    }

    companion object {

        private val serialVersionUID = -6458334957088315646L

        val CREATOR: Parcelable.Creator<MethodReturnItem> = object : Parcelable.Creator<MethodReturnItem> {

            override fun createFromParcel(parcel: Parcel): MethodReturnItem {
                val mci = MethodReturnItem()
                mci.readFromParcel(parcel)
                return mci
            }

            override fun newArray(size: Int): Array<MethodReturnItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}
