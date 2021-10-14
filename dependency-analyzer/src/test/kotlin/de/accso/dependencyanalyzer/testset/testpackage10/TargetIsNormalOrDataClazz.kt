package de.accso.dependencyanalyzer.testset.testpackage10

class ClassUsingTargets {
    val target1 = TargetIsNormalClazz("foo")
    val target2 = TargetIsDataClazz("bar")
}

class TargetIsNormalClazz (val s: String)
class TargetIsDataClazz (val s: String)
