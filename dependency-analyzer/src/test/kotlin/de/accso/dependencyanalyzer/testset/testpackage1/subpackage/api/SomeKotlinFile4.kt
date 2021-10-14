package de.accso.dependencyanalyzer.testset.testpackage1.subpackage.api

import de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI

data class Top4(val ref_a: Middle4, val ref_b: Middle4)
data class Middle4(val ref_x: TargetUsedFromMultipleFilesInAPI, val ref_y: TargetUsedFromMultipleFilesInAPI)
