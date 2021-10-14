package de.accso.dependencyanalyzer.testset.testpackage13

class ClassUsingEnum(val target: TargetIsAnEnum)
class ClassUsingEnumValue(val target: TargetIsAnEnum = TargetIsAnEnum.ONE)

enum class TargetIsAnEnum { ONE, TWO, THREE }
