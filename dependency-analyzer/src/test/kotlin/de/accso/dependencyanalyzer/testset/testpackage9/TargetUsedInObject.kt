package de.accso.dependencyanalyzer.testset.testpackage9

object TopObjectUsingMiddleUsingTarget {
    val t = Middle(TargetUsedInObject("fromObject"))
}

class Middle(val t: TargetUsedInObject)

class TargetUsedInObject (val s: String)
