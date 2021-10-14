package de.accso.dependencyanalyzer

import kotlin.reflect.KClass

// forward analysis - checking _outgoing_ dependencies
// collect transitive dependencies by Reflection, for now based on Kotlin reflection constructor analysis

class DependencyAnalyzerForwardAnalysisViaKotlinReflectionConstructors(
        private val allKClazzesToAnalyze: Set<KClass<*>>) {

    fun forwardAnalysisToFindTransitivelyDependentKClazzesViaConstructors(
        toKClazzes: List<KClass<*>>): Set<DependencyChainForKClazz> {
        val dependentKClazzes = mutableSetOf<DependencyChainForKClazz>()

        toKClazzes.forEach { toKClazz ->
//TODO how to beautify this, how to avoid the mutable collection?
            dependentKClazzes.addAll(
                forwardAnalysisToFindTransitivelyDependentKClazzesViaConstructorRecursion(toKClazz)
            )
        }

        return dependentKClazzes
    }

    private fun forwardAnalysisToFindTransitivelyDependentKClazzesViaConstructorRecursion(
        toKClazz: KClass<*>,
        alreadyAnalyzedKClazzes: MutableSet<KClass<*>> = mutableSetOf()): Set<DependencyChainForKClazz> {
        val dependentKClazzes = mutableSetOf<DependencyChainForKClazz>()

        val clazzesToCheckInRecursion = mutableSetOf<KClass<*>>()
        val mutableAlreadyAnalyzedKClazzes = alreadyAnalyzedKClazzes.plus(toKClazz).toMutableSet()

// TODO optimize, as checks toooo much (allKClazzesToAnalyze - alreadyAnalyzedKClazzes might be enough?)
        allKClazzesToAnalyze.forEach { fromKClazz ->
            if (fromKClazz.usesAsArgumentInConstructor(toKClazz)) {
//TODO how to beautify this, how to avoid the mutable collection?
                dependentKClazzes += DependencyChain(fromKClazz, emptyList() /* TODO */, toKClazz)     //TODO  fill chain to list
                clazzesToCheckInRecursion.add(fromKClazz)
            }
        }

        clazzesToCheckInRecursion.forEach { nextTargetKClazz ->
            // if not already analyzed
            if (!mutableAlreadyAnalyzedKClazzes.contains(nextTargetKClazz)) {
//TODO how to beautify this, how to avoid the mutable collection?
                dependentKClazzes +=
                    forwardAnalysisToFindTransitivelyDependentKClazzesViaConstructorRecursion(nextTargetKClazz,
                                                                                   mutableAlreadyAnalyzedKClazzes) // recursion
                mutableAlreadyAnalyzedKClazzes.add(nextTargetKClazz)
            }
        }

        return dependentKClazzes
    }
}
