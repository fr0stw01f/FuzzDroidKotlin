package de.tu_darmstadt.sse.additionalappclasses.hooking

import java.util.HashSet

import com.morgoo.hook.zhook.MethodHook.MethodHookParam
import com.sun.org.apache.xpath.internal.operations.Bool

import de.tu_darmstadt.sse.sharedclasses.util.Pair

class ConditionalMethodHookBefore(private val paramConditions: Set<ParameterConditionValueInfo>) : AbstractMethodHookBefore() {

    private var valueReplacementNecessary: Boolean? = false

    private val newParamObjectPairs = HashSet<Pair<Int, Any>>()


    fun testConditionSatisfaction(originalMethodInfo: MethodHookParam) {
        for (paramConditionValuePair in paramConditions) {
            val paramCondition = paramConditionValuePair.condition
            if (paramCondition.isConditionSatisfied(originalMethodInfo)) {
                valueReplacementNecessary = true
                val paramIndex = paramConditionValuePair.paramIndex
                val newParamValue = paramConditionValuePair.newValue
                newParamObjectPairs.add(Pair<Int, Any>(paramIndex, newParamValue))
            }
        }
    }

    override fun getParamValuesToReplace(): Set<Pair<Int, Any>>? {
        return newParamObjectPairs
    }

    override fun isValueReplacementNecessary(): Boolean? {
        return valueReplacementNecessary
    }
}
