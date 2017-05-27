package de.tu_darmstadt.sse.additionalappclasses.hooking

import com.morgoo.hook.zhook.MethodHook.MethodHookParam


class ConditionalMethodHookAfter(private val condition: Condition, private val returnValue: Any) : AbstractMethodHookAfter() {

    private var valueReplacementNecessary: Boolean? = null

    fun testConditionSatisfaction(originalMethodInfo: MethodHookParam) {
        valueReplacementNecessary = condition.isConditionSatisfied(originalMethodInfo)
    }

    override fun getReturnValue(): Any? {
        return returnValue
    }

    override fun isValueReplacementNecessary(): Boolean? {
        return valueReplacementNecessary
    }

}
