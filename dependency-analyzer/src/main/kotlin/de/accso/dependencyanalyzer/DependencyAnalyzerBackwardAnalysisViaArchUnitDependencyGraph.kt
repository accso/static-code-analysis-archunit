package de.accso.dependencyanalyzer

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import kotlin.reflect.KClass

// backward analysis - checking _incoming_ dependencies
// to collect transitive dependencies via ArchUnit's dependency graph

class DependencyAnalyzerBackwardAnalysisViaArchUnitDependencyGraph(
        private val analyzeDependenciesOnPackagesWithPrefix: String,
        private val allJavaClazzesToAnalyze: JavaClasses) {

    fun backwardAnalysisToFindTransitivelyDependentJavaClazzes(
        toKClazzes: List<KClass<*>>): Set<DependencyChain>
    {
        val dependentKClazzes = mutableSetOf<DependencyChain>()

        toKClazzes.forEach { targetKClazz ->
            val targetJavaClazz = allJavaClazzesToAnalyze.singleOrNull { it.isEqualToKClazz(targetKClazz) }

            if (targetJavaClazz == null)
                targetKClazz.throwExceptionBecauseJavaClazzNotFoundForKClazz(analyzeDependenciesOnPackagesWithPrefix)
            else
//TODO how to beautify this, how to avoid the mutable collection?
                dependentKClazzes.addAll(
                    backwardAnalysisToFindTransitivelyDependentJavaClazzesRecursion(
                            targetJavaClazz,
                            targetJavaClazz,
                            null
                    ).toSet()
                )
        }

        return dependentKClazzes
    }

    private fun backwardAnalysisToFindTransitivelyDependentJavaClazzesRecursion(
            toJavaClazz: JavaClass,
            toTargetJavaClazz: JavaClass,
            lastDependencyChain: DependencyChain?,
            alreadyAnalyzedJavaClazzes: MutableSet<JavaClass> = mutableSetOf()): Set<DependencyChain>
    {
        val dependencies = mutableSetOf<DependencyChain>()

        val clazzesToCheckInRecursion = mutableSetOf<JavaClass>()
        val mutableAnalyzedJavaClazzes = alreadyAnalyzedJavaClazzes.plus(toJavaClazz).toMutableSet()

        toJavaClazz.directDependenciesToSelf.forEach { dependency ->
//TODO how to beautify this, how to avoid the mutable collection?
            dependencies += if (lastDependencyChain == null) {
                DependencyChain(dependency.originClass.toKClazz(),
                                emptyList(),
                                toTargetJavaClazz.toKClazz())
            }
            else {
                DependencyChain(dependency.originClass.toKClazz(),
                        listOf(lastDependencyChain.from) + lastDependencyChain.dependencies,
                                lastDependencyChain.to
                )
            }
            clazzesToCheckInRecursion.add(dependency.originClass)
        }

        clazzesToCheckInRecursion.forEach { nextTargetJavaClazz ->
            if (!mutableAnalyzedJavaClazzes.alreadyAnalyzed(nextTargetJavaClazz)) {
                val lastDependencyChainForRecursion = if (lastDependencyChain == null) {
                    DependencyChain(nextTargetJavaClazz.toKClazz(), emptyList(), toTargetJavaClazz.toKClazz())
                }
                else {
                    DependencyChain(nextTargetJavaClazz.toKClazz(),
                            listOf(lastDependencyChain.from) + lastDependencyChain.dependencies,
                                         lastDependencyChain.to
                    )
                }

//TODO how to beautify this, how to avoid the mutable collection?
                dependencies +=
                    backwardAnalysisToFindTransitivelyDependentJavaClazzesRecursion( // recursion
                            nextTargetJavaClazz,
                            toTargetJavaClazz,
                            lastDependencyChainForRecursion,
                            mutableAnalyzedJavaClazzes)
                mutableAnalyzedJavaClazzes.add(nextTargetJavaClazz)
            }
        }

        return dependencies.toSet()
    }
}
