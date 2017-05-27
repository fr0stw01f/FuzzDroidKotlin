package de.tu_darmstadt.sse.additionalappclasses.hookdefinitions

import de.tu_darmstadt.sse.additionalappclasses.hooking.HookInfo

interface Hook {
    fun initializeHooks(): Set<HookInfo>
}
