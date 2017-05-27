package de.tu_darmstadt.sse.additionalappclasses.hookdefinitions

import java.util.HashSet

import de.tu_darmstadt.sse.additionalappclasses.hooking.HookInfo
import de.tu_darmstadt.sse.additionalappclasses.hooking.MethodHookInfo

class DexFileExtractorHookDefinitions : Hook {

    override fun initializeHooks(): Set<HookInfo> {
        val dexFileHooks = HashSet<HookInfo>()

        val loadDex = MethodHookInfo("<dalvik.system.DexFile: dalvik.system.DexFile loadDex(java.lang.String, java.lang.String, int)>")
        loadDex.dexFileExtractorHookBefore(0)
        dexFileHooks.add(loadDex)

        return dexFileHooks
    }

}
