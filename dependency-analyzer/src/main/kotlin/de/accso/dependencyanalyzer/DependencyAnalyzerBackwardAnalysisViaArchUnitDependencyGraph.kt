package de.accso.dependencyanalyzer

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import kotlin.reflect.KClass

// backward analysis - checking _incoming_ dependencies
// to collect transitive dependencies via ArchUnit's dependency graph

class DependencyAnalyzerBackwardAnalysisViaArchUnitDependencyGraph(
        private val analyzeDependenciesOnPackagesWithPrefix: String,
        private val allJavaClazzesToAnalyze: JavaClasses) {

    fun backwardAnalysisToFindTransitivelyDependentJavaClazzes(toKClazzes: List<KClass<*>>): Set<DependencyChain> =
        toKClazzes.map { targetKClazz ->
            val targetJavaClazz = allJavaClazzesToAnalyze.singleOrNull { it.isEqualToKClazz(targetKClazz) }

            if (targetJavaClazz == null)
                targetKClazz.throwExceptionBecauseJavaClazzNotFoundForKClazz(analyzeDependenciesOnPackagesWithPrefix)

            backwardAnalysisToFindTransitivelyDependentJavaClazzesRecursion(targetJavaClazz!!)
        }
        .flatten().toSet()

    private fun backwardAnalysisToFindTransitivelyDependentJavaClazzesRecursion(
        finalTargetJavaClazz: JavaClass,
        targetJavaClazz: JavaClass = finalTargetJavaClazz,
        lastDependencyChain: DependencyChain? = null,
        alreadyAnalyzedJavaClazzes: MutableSet<JavaClass> = mutableSetOf()
    ): Set<DependencyChain>
    {
        val dependencies = mutableSetOf<DependencyChain>()
        val clazzesToCheckInRecursion  = mutableSetOf<JavaClass>()
        val mutableAnalyzedJavaClazzes = alreadyAnalyzedJavaClazzes.plus(targetJavaClazz).toMutableSet()

        targetJavaClazz.directDependenciesToSelf.forEach { dependency ->
            dependencies += if (lastDependencyChain == null)
                DependencyChain(dependency.originClass.toKClazz(),
                                emptyList(),
                                finalTargetJavaClazz.toKClazz())
            else
                DependencyChain(dependency.originClass.toKClazz(),
                    listOf(lastDependencyChain.from) + lastDependencyChain.dependencies,
                                lastDependencyChain.to
                )

            clazzesToCheckInRecursion.add(dependency.originClass)
        }

        clazzesToCheckInRecursion.forEach { nextTargetJavaClazz ->
            if (!mutableAnalyzedJavaClazzes.alreadyAnalyzed(nextTargetJavaClazz)) {
                val lastDependencyChainForRecursion = if (lastDependencyChain == null) {
                    DependencyChain(nextTargetJavaClazz.toKClazz(), emptyList(), finalTargetJavaClazz.toKClazz())
                }
                else {
                    DependencyChain(nextTargetJavaClazz.toKClazz(),
                        listOf(lastDependencyChain.from) + lastDependencyChain.dependencies,
                                    lastDependencyChain.to
                    )
                }

                // recursion
                dependencies += backwardAnalysisToFindTransitivelyDependentJavaClazzesRecursion(
                        finalTargetJavaClazz,
                        nextTargetJavaClazz,
                        lastDependencyChainForRecursion,
                        mutableAnalyzedJavaClazzes
                    )
                mutableAnalyzedJavaClazzes.add(nextTargetJavaClazz)
            }
        }

        return dependencies.toSet()
    }
}
