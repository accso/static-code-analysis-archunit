package de.accso.dependencyanalyzer.testset.testpackage1.api

import de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI

// 2 x 2 dependencies to TARGET

data class Top3(val ref_a: Middle3, val ref_b: Middle3)
data class Middle3(val ref_x: TargetUsedFromMultipleFilesInAPI, val ref_y: TargetUsedFromMultipleFilesInAPI)
