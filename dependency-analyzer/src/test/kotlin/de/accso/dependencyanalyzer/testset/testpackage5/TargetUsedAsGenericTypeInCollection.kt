package de.accso.dependencyanalyzer.testset.testpackage5

data class Top1(val ref: Middle1)
data class Middle1(val refs: List<TargetUsedAsGenericTypeInCollection>)

data class Top2(val ref: Middle2)
data class Middle2(val refs: Set<TargetUsedAsGenericTypeInCollection>)

data class Top3(val ref: Middle2)
data class Middle3(val refs: Set<TargetUsedAsGenericTypeInCollection?>)

data class TargetUsedAsGenericTypeInCollection(val s: String)
