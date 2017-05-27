package de.tu_darmstadt.sse.sharedclasses.dynamiccfg

import de.tu_darmstadt.sse.sharedclasses.tracing.TraceItem


abstract class AbstractDynamicCFGItem : TraceItem {

    constructor() : super()

    constructor(lastExecutedStatement: Int) : super(lastExecutedStatement)

    companion object {
        private val serialVersionUID = -5500762826791899632L
    }

}
