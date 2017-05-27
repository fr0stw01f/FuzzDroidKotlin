package de.tu_darmstadt.sse.sharedclasses.dynamiccfg

import android.os.Parcel
import android.os.Parcelable


class MethodCallItem : AbstractDynamicCFGItem {

    constructor() : super()

    constructor(lastExecutedStatement: Int) : super(lastExecutedStatement)

    override fun toString(): String {
        return "Method call: " + lastExecutedStatement
    }

    companion object {
        private val serialVersionUID = -8382002494703671501L

        val CREATOR: Parcelable.Creator<MethodCallItem> = object : Parcelable.Creator<MethodCallItem> {

            override fun createFromParcel(parcel: Parcel): MethodCallItem {
                val mci = MethodCallItem()
                mci.readFromParcel(parcel)
                return mci
            }

            override fun newArray(size: Int): Array<MethodCallItem?> {
                return arrayOfNulls(size)
            }

        }
    }

}
