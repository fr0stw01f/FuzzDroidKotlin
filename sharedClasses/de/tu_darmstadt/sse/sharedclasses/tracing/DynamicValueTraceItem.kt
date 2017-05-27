package de.tu_darmstadt.sse.sharedclasses.tracing

import android.os.Parcel


abstract class DynamicValueTraceItem : TraceItem {

    var paramIdx: Int = 0
        private set

    protected constructor() : super()

    constructor(paramIdx: Int, lastExecutedStatement: Int) : super(lastExecutedStatement) {
        this.paramIdx = paramIdx
    }

    override fun writeToParcel(parcel: Parcel, arg1: Int) {
        super.writeToParcel(parcel, arg1)
        parcel.writeInt(paramIdx)
    }

    override fun readFromParcel(parcel: Parcel) {
        super.readFromParcel(parcel)
        this.paramIdx = parcel.readInt()
    }

    companion object {
        private val serialVersionUID = -8275782123844910280L
    }

}
