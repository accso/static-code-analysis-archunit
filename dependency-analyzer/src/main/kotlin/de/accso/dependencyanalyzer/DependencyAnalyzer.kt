package de.accso.dependencyanalyzer

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import java.util.regex.Pattern
import kotlin.reflect.KClass

class DependencyAnalyzer(private val analyzeDependenciesOnPackagesWithPrefix: String,
                         private val analyzeTestCode: Boolean = false) {

    private val allJavaClazzesToAnalyze: JavaClasses
    private val allKClazzesToAnalyze: Set<KClass<*>>

    init {
        val clazzFileImporter = if (analyzeTestCode) {
            ClassFileImporter()
        } else {
            ClassFileImporter()
                .withImportOption ( ImportOption.Predefined.DO_NOT_INCLUDE_TESTS )
                .withImportOption { location ->
                    location?.matches(Pattern.compile(".*-tests\\.jar.*"))?.not() ?: false
                }
        }

//TODO change to lazy init, only when really needed and accessed
        allJavaClazzesToAnalyze =
            clazzFileImporter
                .importPackages(analyzeDependenciesOnPackagesWithPrefix)
// TODO is "that" needed or is this redundant to the "importPackages" already?
                .that(
                    object: DescribedPredicate<JavaClass>("package starts with given value") {
                        override fun apply(clazz: JavaClass) =
                            clazz.packageName.startsWith(analyzeDependenciesOnPackagesWithPrefix)
                    }
                )

        allKClazzesToAnalyze = allJavaClazzesToAnalyze.toKClazzes()
    }

    // ------------------------------------------------------------------------------------------------------------

    fun clazzesTransitivelyDependentOn(targetKClazz: KClass<*>, filterForPackage: String) =
        clazzesTransitivelyDependentOn(listOf(targetKClazz), listOf(filterForPackage))

    fun clazzesTransitivelyDependentOn(targetKClazz: KClass<*>, filterForPackages: List<String> = emptyList()) =
        clazzesTransitivelyDependentOn(listOf(targetKClazz), filterForPackages)

    fun clazzesTransitivelyDependentOn(targetKClazzes: List<KClass<*>>, filterForPackages: List<String> = emptyList()): Set<KClass<*>> {
        val targetKClazzesAndItSealedSubclazzes = targetKClazzes.map { it.clazzAndItsSealedSubClazzes() }.flatten()

        val backwardAnalysis = DependencyAnalyzerBackwardAnalysisViaArchUnitDependencyGraph(
                        analyzeDependenciesOnPackagesWithPrefix, allJavaClazzesToAnalyze)

        val dependentClazzesFromBackwardAnalysisViaArchUnitDependencyGraph =
                backwardAnalysis.backwardAnalysisToFindTransitivelyDependentJavaClazzes(
                        targetKClazzesAndItSealedSubclazzes)

        return dependentClazzesFromBackwardAnalysisViaArchUnitDependencyGraph.map { it.from }
               .filter { kClazz -> !targetKClazzes.contains(kClazz) } // filter out self
               .filter { kClazz -> (filterForPackages.isEmpty() || kClazz.residesInAnyPackage(filterForPackages)) } // filter only for relevant packages
               .toSet()
    }

    // ------------------------------------------------------------------------------------------------------------

    fun transitiveDependencyChainsOn(targetKClazz: KClass<*>, filterForPackage: String) =
        transitiveDependencyChainsOn(listOf(targetKClazz), listOf(filterForPackage))

    fun transitiveDependencyChainsOn(targetKClazz: KClass<*>, filterForPackages: List<String> = emptyList()) =
        transitiveDependencyChainsOn(listOf(targetKClazz), filterForPackages)

    fun transitiveDependencyChainsOn(targetKClazzes: List<KClass<*>>, filterForPackage: String) =
        transitiveDependencyChainsOn(targetKClazzes, listOf(filterForPackage))

    fun transitiveDependencyChainsOn(targetKClazzes: List<KClass<*>>, filterForPackages: List<String> = emptyList()): Set<DependencyChain> {
        val targetKClazzesAndItSealedSubclazzes = targetKClazzes.map { it.clazzAndItsSealedSubClazzes() }.flatten()

        val backwardAnalysis = DependencyAnalyzerBackwardAnalysisViaArchUnitDependencyGraph(
                analyzeDependenciesOnPackagesWithPrefix, allJavaClazzesToAnalyze)

        val dependentClazzesFromBackwardAnalysisViaArchUnitDependencyGraph =
                backwardAnalysis.backwardAnalysisToFindTransitivelyDependentJavaClazzes(
                        targetKClazzesAndItSealedSubclazzes)

        return  dependentClazzesFromBackwardAnalysisViaArchUnitDependencyGraph
                .filter { kClazz -> !targetKClazzes.contains(kClazz.from) } // filter out self
                .filter { kClazz -> (filterForPackages.isEmpty() || kClazz.from.residesInAnyPackage(filterForPackages)) } // filter only for relevant packages
                .toSet()
    }

}
