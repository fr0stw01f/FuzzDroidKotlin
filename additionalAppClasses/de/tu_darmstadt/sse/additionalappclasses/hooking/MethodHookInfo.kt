package de.tu_darmstadt.sse.additionalappclasses.hooking

import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern

import de.tu_darmstadt.sse.additionalappclasses.util.UtilHook
import de.tu_darmstadt.sse.sharedclasses.util.Pair


class MethodHookInfo(private val methodSignature: String) : HookInfo {
    val className: String
    val methodName: String
    val params: Array<Class<*>>?

    var beforeHooks: MutableList<AbstractMethodHookBefore>? = null
        get
        private set

    var afterHooks: MutableList<AbstractMethodHookAfter>? = null
        get
        private set

    init {
        this.className = getClassName(methodSignature)
        this.methodName = getMethodName(methodSignature)
        this.params = getParams(methodSignature)
    }

    private fun getClassName(methodSignature: String): String {
        val pattern = "<(.*):.*>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(methodSignature)
        if (m.find()) {
            return m.group(1)
        } else {
            throw RuntimeException("wrong format for className")
        }
    }

    private fun getMethodName(methodSignature: String): String {
        val pattern = "<.*\\s(.*)\\(.*>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(methodSignature)
        if (m.find()) {
            return m.group(1)
        } else {
            throw RuntimeException("wrong format for className")
        }
    }

    private fun getParams(methodSignature: String): Array<Class<*>>? {
        val pattern = "<.*\\((.*)\\)>"
        val r = Pattern.compile(pattern)

        val m = r.matcher(methodSignature)
        if (m.find()) {
            val allParamTypes = m.group(1)
            //no params
            if (allParamTypes == "")
                return null
            else {
                val classTypes = allParamTypes.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return UtilHook.getClassTypes(classTypes)
            }

        } else {
            throw RuntimeException("wrong format for param-type")
        }
    }

    fun persistentHookBefore(pairs: Set<Pair<Int, Any>>) {
        if (this.beforeHooks == null)
            this.beforeHooks = ArrayList<AbstractMethodHookBefore>()
        this.beforeHooks!!.add(PersistentMethodHookBefore(pairs))
    }

    fun analysisDependentHookBefore() {
        if (this.beforeHooks == null)
            this.beforeHooks = ArrayList<AbstractMethodHookBefore>()
        this.beforeHooks!!.add(AnalysisDependentMethodHookBefore(this.methodSignature))
    }

    fun conditionDependentHookBefore(paramConditions: Set<ParameterConditionValueInfo>) {
        if (this.beforeHooks == null)
            this.beforeHooks = ArrayList<AbstractMethodHookBefore>()
        this.beforeHooks!!.add(ConditionalMethodHookBefore(paramConditions))
    }

    fun persistentHookAfter(returnValue: Any) {
        if (this.afterHooks == null)
            this.afterHooks = ArrayList<AbstractMethodHookAfter>()
        this.afterHooks!!.add(PersistentMethodHookAfter(returnValue))
    }

    fun analysisDependentHookAfter() {
        if (this.afterHooks == null)
            this.afterHooks = ArrayList<AbstractMethodHookAfter>()
        this.afterHooks!!.add(AnalysisDependentMethodHookAfter(this.methodSignature))
    }

    fun dexFileExtractorHookBefore(argumentPosition: Int) {
        if (this.beforeHooks == null)
            this.beforeHooks = ArrayList<AbstractMethodHookBefore>()
        this.beforeHooks!!.add(DexFileExtractorHookBefore(this.methodSignature, argumentPosition))
    }

    fun simpleBooleanHookAfter() {
        if (this.afterHooks == null)
            this.afterHooks = ArrayList<AbstractMethodHookAfter>()
        this.afterHooks!!.add(SimpleBooleanHookAfter(this.methodSignature))
    }

    fun conditionDependentHookAfter(condition: Condition, returnValue: Any) {
        if (this.afterHooks == null)
            this.afterHooks = ArrayList<AbstractMethodHookAfter>()
        this.afterHooks!!.add(ConditionalMethodHookAfter(condition, returnValue))
    }

    fun hasHookBefore(): Boolean {
        return beforeHooks != null
    }

    fun hasHookAfter(): Boolean {
        return afterHooks != null
    }

}
