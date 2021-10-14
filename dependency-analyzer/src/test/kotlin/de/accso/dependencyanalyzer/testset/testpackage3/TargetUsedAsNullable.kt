package de.accso.dependencyanalyzer.testset.testpackage3

data class Top(val ref: Middle?)
data class Middle(val ref: TargetUsedAsNullable?)

class TargetUsedAsNullable(val s: String)
