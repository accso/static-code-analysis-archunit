package de.accso.dependencyanalyzer.testset.testpackage1.domain

import de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedInMultipleFilesInDomain

data class Top2(val ref: Middle2)
data class Middle2(val ref: TargetUsedInMultipleFilesInDomain)

data class YetAnotherClass(val s: String)
