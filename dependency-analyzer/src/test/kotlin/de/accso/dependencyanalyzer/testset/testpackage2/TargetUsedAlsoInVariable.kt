package de.accso.dependencyanalyzer.testset.testpackage2

data class Top(val ref: Middle)
data class Middle(val ref: TargetIsUsedAlsoInVariable)

data class TargetIsUsedAlsoInVariable(val s: String)

val fooVariable = Top(Middle(TargetIsUsedAlsoInVariable("foo")))

