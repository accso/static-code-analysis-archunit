package de.accso.dependencyanalyzer

import com.tngtech.archunit.core.domain.JavaClass
import kotlin.reflect.KClass

data class DependencyChain<T>(val from: T, val dependencies: List<T>, val to: T?)

typealias DependencyChainForJavaClass = DependencyChain<KClass<*>>

data class DependencyChainForKClazz(val from: KClass<*>, val dependencies: List<KClass<*>>, val to: KClass<*>?)
/*{
    override fun toString(): String {
        val dependenciesToString =
                if(dependencies.isEmpty()) ""
                else dependencies.joinToString(" -> ", "-> ") { it.qualifiedName.toString() }
        return "${from.qualifiedName} " + dependenciesToString + "-> ${to?.qualifiedName}"
    }
}
*/