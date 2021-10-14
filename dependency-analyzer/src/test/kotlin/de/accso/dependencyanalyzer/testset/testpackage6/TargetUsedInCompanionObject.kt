package de.accso.dependencyanalyzer.testset.testpackage6

class ClazzHavingCompanionObjectUsingTarget {
    companion object {
        val target = TargetUsedInCompanionObject("foo")
    }
}

class TargetUsedInCompanionObject(val s: String)