package de.accso.dependencyanalyzer.testset.testpackage12

class ClassUsingTarget(val target: TargetAlias)

class Target (val s: String)
typealias TargetAlias = Target
