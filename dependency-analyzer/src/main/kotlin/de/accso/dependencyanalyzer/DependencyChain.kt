package de.accso.dependencyanalyzer

import com.tngtech.archunit.core.domain.JavaClass
import kotlin.reflect.KClass

data class DependencyChain<T>(val from: T, val dependencies: List<T>, val to: T?)

typealias DependencyChainForKClazz    = DependencyChain<KClass<*>>
typealias DependencyChainForJavaClass = DependencyChain<JavaClass>
