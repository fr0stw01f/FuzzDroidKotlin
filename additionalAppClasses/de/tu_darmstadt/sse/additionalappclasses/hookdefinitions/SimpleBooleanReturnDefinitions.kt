package de.tu_darmstadt.sse.additionalappclasses.hookdefinitions

import java.util.HashSet

import de.tu_darmstadt.sse.additionalappclasses.hooking.HookInfo
import de.tu_darmstadt.sse.additionalappclasses.hooking.MethodHookInfo

class SimpleBooleanReturnDefinitions : Hook {

    override fun initializeHooks(): Set<HookInfo> {
        val booleanHooks = HashSet<HookInfo>()

        val getBooleanSP = MethodHookInfo("<android.app.SharedPreferencesImpl: boolean getBoolean(java.lang.String, boolean)>")
        getBooleanSP.simpleBooleanHookAfter()
        booleanHooks.add(getBooleanSP)

        return booleanHooks
    }

}
