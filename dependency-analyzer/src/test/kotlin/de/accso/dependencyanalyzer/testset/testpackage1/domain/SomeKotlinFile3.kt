package de.accso.dependencyanalyzer.testset.testpackage1.domain

import de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedInMultipleFilesInDomain

// 2 x 2 dependencies to TARGET

data class Top3(val ref_a: Middle3, val ref_b: Middle3)
data class Middle3(val ref_x: TargetUsedInMultipleFilesInDomain, val ref_y: TargetUsedInMultipleFilesInDomain)
