package de.tu_darmstadt.sse.bootstrap

import java.util.HashSet


class AnalysisTask : Cloneable {

    internal val dexFilesToMerge = HashSet<DexFile>()
    internal val statementsToRemove = HashSet<InstanceIndependentCodePosition>()


    @JvmOverloads fun deriveNewTask(fileToMerge: DexFile,
                                    toRemove: Set<InstanceIndependentCodePosition>? = null): AnalysisTask {
        val newTask = clone()
        newTask.dexFilesToMerge.add(fileToMerge)
        if (toRemove != null)
            newTask.statementsToRemove.addAll(toRemove)
        return newTask
    }

    public override fun clone(): AnalysisTask {
        val clone = AnalysisTask()
        clone.dexFilesToMerge.addAll(dexFilesToMerge)
        return clone
    }


    fun getDexFilesToMerge(): Set<DexFile> {
        return this.dexFilesToMerge
    }


    fun getStatementsToRemove(): Set<InstanceIndependentCodePosition> {
        return this.statementsToRemove
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + dexFilesToMerge.hashCode()
        result = prime * result + statementsToRemove.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as AnalysisTask?
        if (dexFilesToMerge == null) {
            if (other!!.dexFilesToMerge != null)
                return false
        } else if (dexFilesToMerge != other!!.dexFilesToMerge)
            return false
        if (statementsToRemove == null) {
            if (other.statementsToRemove != null)
                return false
        } else if (statementsToRemove != other.statementsToRemove)
            return false
        return true
    }

}
