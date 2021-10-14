package de.accso.dependencyanalyzer

import java.lang.reflect.Type

fun Type.isEqualTo(other: Type) = this.typeName == other.typeName

//TODO is this safe enough? whatabout generic type pairs or tupels?
fun Type.usesAsGenericType(other: Type) = this.typeName.contains("<"+other.typeName+">")
