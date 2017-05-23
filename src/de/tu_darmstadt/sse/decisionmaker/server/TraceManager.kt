package de.tu_darmstadt.sse.decisionmaker.server

import java.util.*
import java.util.concurrent.ConcurrentHashMap


class TraceManager {

    private val threadToManager = ConcurrentHashMap<Long, ThreadTraceManager>()
    private val onCreateHandler = HashSet<ThreadTraceManagerCreatedHandler>()


    val allThreadTraceManagers: Collection<ThreadTraceManager>
        get() = threadToManager.values


    fun getThreadTraceManager(threadId: Long): ThreadTraceManager {
        return threadToManager[threadId]!!
    }


    fun getOrCreateThreadTraceManager(threadId: Long): ThreadTraceManager {
        val newManager = ThreadTraceManager(threadId)
        val existingManager = threadToManager.putIfAbsent(threadId, newManager)
        //val existingManager = (threadToManager as java.util.Map<Long, ThreadTraceManager>).putIfAbsent(threadId, newManager)
        if (existingManager == null) {
            for (handler in onCreateHandler)
                handler.onThreadTraceManagerCreated(newManager)
            return newManager
        } else
            return existingManager
    }


    fun addThreadTraceCreateHandler(handler: ThreadTraceManagerCreatedHandler) {
        this.onCreateHandler.add(handler)
    }

}//
