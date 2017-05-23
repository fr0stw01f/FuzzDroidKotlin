package de.tu_darmstadt.sse.bootstrap

import java.util.concurrent.ConcurrentHashMap


class DexFileManager {

    private val dexFiles = ConcurrentHashMap<DexFile, DexFile>()


    fun add(dexFile: DexFile): DexFile {
        val ret = (dexFiles as MutableMap<DexFile, DexFile>).putIfAbsent(dexFile, dexFile)
        return ret ?: dexFile
    }

}
