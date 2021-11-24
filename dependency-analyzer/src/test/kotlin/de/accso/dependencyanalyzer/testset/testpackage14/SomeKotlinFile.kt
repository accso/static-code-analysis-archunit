package de.accso.dependencyanalyzer.testset.testpackage14

data class Top1(val ref1: Middle1, val ref2: Middle2)
data class Top2(val ref1: Middle1, val ref2: Middle2)

data class Middle1(val ref: Bottom)
data class Middle2(val ref: Bottom)

data class Bottom(val s: String)

