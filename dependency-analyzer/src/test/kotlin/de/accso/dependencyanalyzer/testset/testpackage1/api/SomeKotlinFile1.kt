package de.accso.dependencyanalyzer.testset.testpackage1.api

import de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI

data class Top1(val ref: Middle1)
data class Middle1(val ref: TargetUsedFromMultipleFilesInAPI)

data class AnotherClass(val s: String)
