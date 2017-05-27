package de.tu_darmstadt.sse.additionalappclasses.tracing

import de.tu_darmstadt.sse.sharedclasses.tracing.TraceItem


internal interface ITracingServiceInterface {

    fun dumpQueue()

    fun enqueueTraceItem(ti: TraceItem)

}
