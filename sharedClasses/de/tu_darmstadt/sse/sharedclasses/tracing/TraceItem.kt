package de.tu_darmstadt.sse.sharedclasses.tracing

import android.os.Parcel
import android.os.Parcelable
import de.tu_darmstadt.sse.sharedclasses.networkconnection.IClientRequest


abstract class TraceItem : Parcelable, IClientRequest {

    var lastExecutedStatement: Int = 0
        private set
    private var globalLastExecutedStatement: Int = 0

    protected constructor() : super()

    @JvmOverloads protected constructor(lastExecutedStatement: Int, globalLastExecutedStatement: Int = -1) {
        this.lastExecutedStatement = lastExecutedStatement
        this.globalLastExecutedStatement = globalLastExecutedStatement
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, arg1: Int) {
        parcel.writeInt(lastExecutedStatement)
        parcel.writeInt(globalLastExecutedStatement)
    }


    protected open fun readFromParcel(parcel: Parcel) {
        this.lastExecutedStatement = parcel.readInt()
        this.globalLastExecutedStatement = parcel.readInt()
    }

    companion object {
        private val serialVersionUID = 5704527703779833243L
    }

}
