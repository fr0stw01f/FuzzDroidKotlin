package de.tu_darmstadt.sse.progressmetric

import java.util.ArrayList
import java.util.HashSet

import soot.Unit
import soot.jimple.infoflow.solver.cfg.InfoflowCFG
import de.tu_darmstadt.sse.decisionmaker.server.history.ClientHistory
import de.tu_darmstadt.sse.sharedclasses.util.Pair


class SubpathNCoverage(targetUnits: Collection<Unit>,
                       val fragmentLength: Int) : IProgressMetric {

    private val coveredPathFragment = HashSet<List<Pair<Unit, Boolean>>>()

    private var cfg: InfoflowCFG? = null


    constructor(targetUnits: Collection<Unit>, cfg: InfoflowCFG) : this(targetUnits, 2) {
        this.cfg = cfg
    }


    override fun update(history: ClientHistory): Int {
        val trace = history.getPathTrace()
        var retval = 0

        if (fragmentLength > 0 && trace.size > fragmentLength) { //coverage of path fragments of length = fragmentLength
            retval += (0..trace.size - fragmentLength - 1)
                    .filter { update(trace.subList(it, it + fragmentLength)) }
                    .count()
        } else if (update(trace)) { //path coverage
            retval++
        }

        history.setProgressValue(getMetricIdentifier(), numCovered)
        return numCovered
    }

    private fun update(l: List<Pair<Unit, Boolean>>): Boolean {
        var retval = false
        if (coveredPathFragment.add(ArrayList(l))) {
            retval = true
        }
        return retval
    }


    val numCovered: Int
        get() = coveredPathFragment.size

    override fun getMetricName(): String {
        return "SubpathNCoverage"
    }

    override fun getMetricIdentifier(): String {
        return "SubpathNCoverage"
    }

    override fun initialize() {
        // TODO Auto-generated method stub
    }

    override fun setCurrentTargetLocation(currentTargetLocation: Unit) {
        // TODO Auto-generated method stub

    }
}
