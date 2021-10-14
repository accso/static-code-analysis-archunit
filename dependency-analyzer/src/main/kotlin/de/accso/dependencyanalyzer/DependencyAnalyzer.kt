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

    fun clazzesTransitivelyDependentOn(targetKClazzes: List<KClass<*>>, filterForPackage: String) =
        clazzesTransitivelyDependentOn(targetKClazzes, listOf(filterForPackage))


//TODO change return type from Set<KClass<*>> to DependencyChain
    fun clazzesTransitivelyDependentOn(targetKClazzes: List<KClass<*>>, filterForPackages: List<String> = emptyList()): Set<KClass<*>> {
        val targetKClazzesAndItSealedSubclazzes = targetKClazzes.map { it.clazzAndItsSealedSubClazzes() }.flatten()

        val backwardAnalysis = DependencyAnalyzerBackwardAnalysisViaArchUnitDependencyGraph(
                        analyzeDependenciesOnPackagesWithPrefix, allJavaClazzesToAnalyze)
//TODO make optional, with some flag "deep analysis yes/no"
        val forwardAnalysis  = DependencyAnalyzerForwardAnalysisViaKotlinReflectionConstructors(
                        allKClazzesToAnalyze)

        val dependentClazzesFromBackwardAnalysisViaArchUnitDependencyGraph =
                backwardAnalysis.backwardAnalysisToFindTransitivelyDependentJavaClazzesViaDependencyGraph(
                        targetKClazzesAndItSealedSubclazzes)
        val dependentClazzesFromForwardAnalysisViaKotlinReflectionConstructors =
                forwardAnalysis.forwardAnalysisToFindTransitivelyDependentKClazzesViaConstructors(
                        targetKClazzesAndItSealedSubclazzes)

//TODO change to DependencyChain, not only from
        return (  (dependentClazzesFromBackwardAnalysisViaArchUnitDependencyGraph   .map { it.from.toKClazz() })
                + (dependentClazzesFromForwardAnalysisViaKotlinReflectionConstructors.map { it.from }))
            .filter { kClazz -> !targetKClazzes.contains(kClazz) } // filter out self
            .filter { kClazz -> (filterForPackages.isEmpty() || kClazz.residesInAnyPackage(filterForPackages)) } // filter only for relevant packages
            .toSet()
    }
}
