package de.accso.dependencyanalyzer.testset.testpackage7

abstract class TargetIsAbstractSuperClazz(val s: String)
abstract class SubClazzOfTargetIsAbstractSuperClazz: TargetIsAbstractSuperClazz("foo")
class SubSubClazzOfTargetIsAbstractSuperClazz: SubClazzOfTargetIsAbstractSuperClazz()

open class TargetIsOpenSuperClazz(val s: String)
open class SubClazzOfTargetIsOpenSuperClazz: TargetIsOpenSuperClazz("bar")
open class SubSubClazzOfTargetIsOpenSuperClazz: SubClazzOfTargetIsOpenSuperClazz()
