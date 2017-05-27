package de.tu_darmstadt.sse.sharedclasses.dynamiccfg

import android.os.Parcel
import android.os.Parcelable


class MethodLeaveItem : AbstractDynamicCFGItem {

    constructor() : super()

    constructor(lastExecutedStatement: Int) : super(lastExecutedStatement)

    override fun toString(): String {
        return "Method leave: " + lastExecutedStatement
    }

    companion object {
        private val serialVersionUID = -8382002494703671501L

        val CREATOR: Parcelable.Creator<MethodLeaveItem> = object : Parcelable.Creator<MethodLeaveItem> {

            override fun createFromParcel(parcel: Parcel): MethodLeaveItem {
                val mci = MethodLeaveItem()
                mci.readFromParcel(parcel)
                return mci
            }

            override fun newArray(size: Int): Array<MethodLeaveItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}
