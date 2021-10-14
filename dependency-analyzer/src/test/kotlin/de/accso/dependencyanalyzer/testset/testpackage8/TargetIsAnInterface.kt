package de.accso.dependencyanalyzer.testset.testpackage8

open class ClazzImplementingTheTarget: TargetIsAnInterface {
    override fun foo() = "foo"
    override val bar: String
        get() = "bar"
}

class SubClazzOfClazzImplementingTheTarget: ClazzImplementingTheTarget() {
    override fun foo() = "foo"
    override val bar: String
        get() = "bar"
}

class SomeClazzUsingTheInterface {
    var target: TargetIsAnInterface? = null
}

class SomeClazzUsingTargetWithAnonymousImplementation {
    val target = object: TargetIsAnInterface {
        override fun foo() = "foo"
        override val bar = "bar"
    }
}

interface TargetIsAnInterface {
    fun foo(): String
    val bar: String
}
