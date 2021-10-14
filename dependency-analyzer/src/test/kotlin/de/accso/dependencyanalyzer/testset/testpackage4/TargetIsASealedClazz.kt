package de.accso.dependencyanalyzer.testset.testpackage4

data class Top1(val ref: Middle1)
data class Middle1(val ref: TargetIsASealedClazz)

data class Top2(val ref2a: Middle2a, val ref2b: Middle2b, val ref2c: Middle2c)
data class Middle2a(val ref: TargetIsASealedClazz.TargetIsASealedClazz1)
data class Middle2b(val ref: TargetIsASealedClazz.TargetIsASealedClazz2)
data class Middle2c(val ref: TargetIsASealedClazz.TargetIsASealedClazz3)

sealed class TargetIsASealedClazz(val s: String, val x: Int) {
    data class TargetIsASealedClazz1(val t: String) : TargetIsASealedClazz(t, 8_1_5)
    data class TargetIsASealedClazz2(val u: String) : TargetIsASealedClazz(u, 99)
    object TargetIsASealedClazz3 : TargetIsASealedClazz("bar", 4711)
}

