package de.accso.dependencyanalyzer.testset.testpackage11

class ClassUsingTargetInArray {
    val targetInArray = arrayOf(TargetIsUsedInCollection("foo"))
}

class ClassUsingTargetInList {
    val targetInArray = listOf(TargetIsUsedInCollection("foo"))
}

class ClassUsingTargetInSet {
    val targetInArray = setOf(TargetIsUsedInCollection("foo"))
}

class TargetIsUsedInCollection (val s: String)
