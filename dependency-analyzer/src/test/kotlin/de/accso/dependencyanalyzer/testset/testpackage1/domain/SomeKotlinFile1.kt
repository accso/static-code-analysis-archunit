package de.accso.dependencyanalyzer.testset.testpackage1.domain

import de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedInMultipleFilesInDomain

data class Top1(val ref: Middle1)
data class Middle1(val ref: TargetUsedInMultipleFilesInDomain)

data class AnotherClass(val s: String)
