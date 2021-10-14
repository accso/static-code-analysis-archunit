package de.accso.dependencyanalyzer.testset.testpackage1.api

import de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI

data class Top2(val ref: Middle2)
data class Middle2(val ref: TargetUsedFromMultipleFilesInAPI)

data class YetAnotherClass(val s: String)
