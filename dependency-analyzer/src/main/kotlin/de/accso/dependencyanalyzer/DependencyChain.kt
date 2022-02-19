package de.accso.dependencyanalyzer

import kotlin.reflect.KClass

data class DependencyChain(val from: KClass<*>, val dependencies: List<KClass<*>>, val to: KClass<*>?) {
    override fun toString(): String {
        val dependenciesToString =
                if(dependencies.isEmpty()) ""
                else dependencies.joinToString(" -> ", " -> ") { it.qualifiedName.toString() }
        return "${from.qualifiedName}" + dependenciesToString + " -> ${to?.qualifiedName}"
    }
}
