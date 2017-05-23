package de.tu_darmstadt.sse.apkspecific.CodeModel

import de.tu_darmstadt.sse.appinstrumentation.UtilInstrumenter
import de.tu_darmstadt.sse.appinstrumentation.transformer.InstrumentedCodeTag
import soot.Scene
import soot.SootMethod
import soot.Unit
import java.util.*


class StaticCodeIndexer {

    private val unitToMethod = HashMap<Unit, SootMethod>()

    init {
        initializeUnitToMethod()
    }


    private fun initializeUnitToMethod() {
        Scene.v().applicationClasses
                .filter { UtilInstrumenter.isAppDeveloperCode(it) && it.isConcrete }
                .forEach { sc ->
                    sc.methods
                            .filter { it.isConcrete }
                            .forEach { sm ->
                                sm.retrieveActiveBody().units
                                        .filterNot { it.hasTag(InstrumentedCodeTag.name) }
                                        .forEach { unitToMethod.put(it, sm) }
                            }
                }
    }


    fun getMethodOf(u: Unit): SootMethod? {
        return unitToMethod[u]
    }

}
