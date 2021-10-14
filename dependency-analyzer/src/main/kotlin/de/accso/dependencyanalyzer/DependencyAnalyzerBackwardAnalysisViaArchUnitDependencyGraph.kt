package de.accso.dependencyanalyzer

import com.tngtech.archunit.core.domain.Dependency
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import kotlin.reflect.KClass

// backward analysis - checking _incoming_ dependencies
// to collect transitive dependencies via ArchUnit's dependency graph

class DependencyAnalyzerBackwardAnalysisViaArchUnitDependencyGraph(
        private val analyzeDependenciesOnPackagesWithPrefix: String,
        private val allJavaClazzesToAnalyze: JavaClasses) {

    fun backwardAnalysisToFindTransitivelyDependentJavaClazzesViaDependencyGraph(
        toKClazzes: List<KClass<*>>): Set<DependencyChainForJavaClass> {
        val dependentKClazzes = mutableSetOf<DependencyChainForJavaClass>()

        toKClazzes.forEach { targetKClazz ->
            val targetJavaClazz = allJavaClazzesToAnalyze.singleOrNull { it.isEqualToKClazz(targetKClazz) }

            if (targetJavaClazz == null)
                targetKClazz.throwExceptionBecauseJavaClazzNotFoundForKClazz(analyzeDependenciesOnPackagesWithPrefix)
            else
//TODO how to beautify this, how to avoid the mutable collection?
                dependentKClazzes.addAll(
                    backwardAnalysisToFindTransitivelyDependentJavaClazzesViaDependencyGraphRecursion(targetJavaClazz)
                        .map { DependencyChainForJavaClass(it.originClass, emptyList() /* TODO */, targetJavaClazz) } //TODO fill chain to list
                        .toSet()
                )
        }

        return dependentKClazzes
    }

    private fun backwardAnalysisToFindTransitivelyDependentJavaClazzesViaDependencyGraphRecursion(
        toJavaClazz: JavaClass,
        alreadyAnalyzedJavaClazzes: MutableSet<JavaClass> = mutableSetOf()): Set<Dependency> {
        val dependencies = mutableSetOf<Dependency>()

        val clazzesToCheckInRecursion = mutableSetOf<JavaClass>()
        val mutableAnalyzedJavaClazzes = alreadyAnalyzedJavaClazzes.plus(toJavaClazz).toMutableSet()

        toJavaClazz.directDependenciesToSelf.forEach { dependency ->
//TODO how to beautify this, how to avoid the mutable collection?
            dependencies += dependency
            clazzesToCheckInRecursion.add(dependency.originClass)
        }

        clazzesToCheckInRecursion.forEach { nextTargetJavaClazz ->
            if (!mutableAnalyzedJavaClazzes.alreadyAnalyzed(nextTargetJavaClazz)) {
//TODO how to beautify this, how to avoid the mutable collection?
                dependencies +=
                    backwardAnalysisToFindTransitivelyDependentJavaClazzesViaDependencyGraphRecursion(nextTargetJavaClazz,
                                                                                mutableAnalyzedJavaClazzes) // recursion
                mutableAnalyzedJavaClazzes.add(nextTargetJavaClazz)
            }
        }

        return dependencies.toSet()
    }
}
