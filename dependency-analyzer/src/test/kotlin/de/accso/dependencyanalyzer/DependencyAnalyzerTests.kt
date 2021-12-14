package de.accso.dependencyanalyzer

import de.accso.dependencyanalyzer.testset.testpackage14.Middle1
import de.accso.dependencyanalyzer.testset.testpackage14.Middle2
import de.accso.dependencyanalyzer.testset.testpackage14.Top1
import de.accso.dependencyanalyzer.testset.testpackage14.Top2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DependencyAnalyzerTests {

    companion object {
        const val TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED = "de.accso.dependencyanalyzer.testset"

        const val API_WILDCARD = "..api.."
        const val DOMAIN_WILDCARD = "..domain.."
    }

    @Test
    fun `find transitive dependending API classes also in several files and also with duplicate dependencies`() {
        // arrange
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI::class

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz, API_WILDCARD)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage1.api.Top1::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Middle1::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Top2::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Middle2::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Top3::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Middle3::class,
                de.accso.dependencyanalyzer.testset.testpackage1.subpackage.api.Top4::class,
                de.accso.dependencyanalyzer.testset.testpackage1.subpackage.api.Middle4::class
            )
        )
    }

    @Test
    fun `find transitive dependending domain classes also in several files and also with duplicate dependencies`() {
        // arrange
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedInMultipleFilesInDomain::class

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz, listOf(DOMAIN_WILDCARD))
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage1.domain.Top1::class,
                de.accso.dependencyanalyzer.testset.testpackage1.domain.Middle1::class,
                de.accso.dependencyanalyzer.testset.testpackage1.domain.Top2::class,
                de.accso.dependencyanalyzer.testset.testpackage1.domain.Middle2::class,
                de.accso.dependencyanalyzer.testset.testpackage1.domain.Top3::class,
                de.accso.dependencyanalyzer.testset.testpackage1.domain.Middle3::class
            )
        )
    }

    @Test
    fun `filter exactly via the package name and also filter out sub packages`() {
        // arrange
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI::class

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz,
                listOf(de.accso.dependencyanalyzer.testset.testpackage1.api.Top1::class.java.packageName)
        )
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage1.api.Top1::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Middle1::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Top2::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Middle2::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Top3::class,
                de.accso.dependencyanalyzer.testset.testpackage1.api.Middle3::class
            )
        )
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "..does.not.exist..",
        "de.accso.doesnotexist",
        "de.accso.doesnotexist..",
    ])
    fun `filter all out as filter package names do not exist`(nonExistingPackage: String) {
        // arrange
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI::class

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz,
                listOf(nonExistingPackage))
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEmpty()
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "de.accso.module1..",
        "de.accso.module2..",
    ])
    fun `filter all out as the filter package name cannot have a dependency to the target`(filterForPackage: String) {
        // arrange
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI::class

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz,
                listOf(filterForPackage))
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEmpty()
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "accso.de",
        "my.package"
    ])
    fun `throws an exception as the class to be analyzed is not found`(analyzeNonExistingPackagePrefix: String) {
        // arrange
        val sut = DependencyAnalyzer(analyzeNonExistingPackagePrefix, true)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI::class

        // act & assert
        assertThrows<IllegalArgumentException> {
            sut.clazzesTransitivelyDependentOn(targetKClazz)
        }
    }

    @Test
    fun `throws an exception as the class to be analyzed is not found when test code is not analyzed at all`() {
        // arrange
        val analyzeTestCode = false
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, analyzeTestCode)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI::class

        // act & assert
        assertThrows<IllegalArgumentException> {
            sut.clazzesTransitivelyDependentOn(targetKClazz)
        }
    }

    @Test
    fun `find transitive dependending domain classes including the Kt file`() {
        // arrange

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage2.TargetIsUsedAlsoInVariable::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).containsAll(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage2.Top::class,
                de.accso.dependencyanalyzer.testset.testpackage2.Middle::class,
            )
        )
        assertThat(dependentClazzes.map { it.qualifiedName }).contains(
            "de.accso.dependencyanalyzer.testset.testpackage2.TargetUsedAlsoInVariableKt"
        )
    }

    @Test
    fun `find transitive dependendencies including Nullability`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage3.TargetUsedAsNullable::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage3.Top::class,
                de.accso.dependencyanalyzer.testset.testpackage3.Middle::class
            )
        )
    }

    @Test
    fun `find transitive dependendencies to a Sealed class including all sealed sub classes`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage4.TargetIsASealedClazz::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage4.Top1::class,
                de.accso.dependencyanalyzer.testset.testpackage4.Middle1::class,
                de.accso.dependencyanalyzer.testset.testpackage4.Top2::class,
                de.accso.dependencyanalyzer.testset.testpackage4.Middle2a::class,
                de.accso.dependencyanalyzer.testset.testpackage4.Middle2b::class,
                de.accso.dependencyanalyzer.testset.testpackage4.Middle2c::class
            ) + targetKClazz.sealedSubclasses.toSet()
        )
    }

    @Test
    fun `find transitive dependendencies to a sealed sub class`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage4.TargetIsASealedClazz.TargetIsASealedClazz1::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage4.Top2::class,
                de.accso.dependencyanalyzer.testset.testpackage4.Middle2a::class
            )
        )
    }

    @Test
    fun `find transitive dependendencies also via the generic type of a collection`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage5.TargetUsedAsGenericTypeInCollection::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage5.Top1::class,
                de.accso.dependencyanalyzer.testset.testpackage5.Middle1::class,
                de.accso.dependencyanalyzer.testset.testpackage5.Top2::class,
                de.accso.dependencyanalyzer.testset.testpackage5.Middle2::class,
                de.accso.dependencyanalyzer.testset.testpackage5.Top3::class,
                de.accso.dependencyanalyzer.testset.testpackage5.Middle3::class
            )
        )
    }

    @Test
    fun `find transitive dependendencies from a companion object`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage6.TargetUsedInCompanionObject::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage6.ClazzHavingCompanionObjectUsingTarget::class,
                de.accso.dependencyanalyzer.testset.testpackage6.ClazzHavingCompanionObjectUsingTarget.Companion::class
            )
        )
    }

    @Test
    fun `find transitive dependendencies via inheritance from a super class`() {
        // arrange
        val targetKClazz1 = de.accso.dependencyanalyzer.testset.testpackage7.TargetIsOpenSuperClazz::class
        val targetKClazz2 = de.accso.dependencyanalyzer.testset.testpackage7.TargetIsAbstractSuperClazz::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(listOf(targetKClazz1, targetKClazz2))
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage7.SubClazzOfTargetIsAbstractSuperClazz::class,
                de.accso.dependencyanalyzer.testset.testpackage7.SubSubClazzOfTargetIsAbstractSuperClazz::class,
                de.accso.dependencyanalyzer.testset.testpackage7.SubClazzOfTargetIsOpenSuperClazz::class,
                de.accso.dependencyanalyzer.testset.testpackage7.SubSubClazzOfTargetIsOpenSuperClazz::class
            )
        )
    }

    @Test
    fun `find transitive dependendencies to an interface`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage8.TargetIsAnInterface::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).containsAll(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage8.ClazzImplementingTheTarget::class,
                de.accso.dependencyanalyzer.testset.testpackage8.SubClazzOfClazzImplementingTheTarget::class,
                de.accso.dependencyanalyzer.testset.testpackage8.SomeClazzUsingTheInterface::class,
                de.accso.dependencyanalyzer.testset.testpackage8.SomeClazzUsingTargetWithAnonymousImplementation::class // auch SomeClazzUsingTargetWithAnonymousImplementation$target$1
            )
        )
    }

    @Test
    fun `find transitive dependendencies from an object`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage9.TargetUsedInObject::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage9.TopObjectUsingMiddleUsingTarget::class,
                de.accso.dependencyanalyzer.testset.testpackage9.Middle::class
            )
        )
    }

    @Test
    fun `find transitive dependendencies to a data class and a normal class`() {
        // arrange
        val targetKClazz1 = de.accso.dependencyanalyzer.testset.testpackage10.TargetIsNormalClazz::class
        val targetKClazz2 = de.accso.dependencyanalyzer.testset.testpackage10.TargetIsDataClazz::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(listOf(targetKClazz1, targetKClazz2))
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage10.ClassUsingTargets::class
            )
        )
    }

    @Test
    fun `find transitive dependendencies also from array or a list or a set`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage11.TargetIsUsedInCollection::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage11.ClassUsingTargetInArray::class,
                de.accso.dependencyanalyzer.testset.testpackage11.ClassUsingTargetInList::class,
                de.accso.dependencyanalyzer.testset.testpackage11.ClassUsingTargetInSet::class
            )
        )
    }

    @Test
    fun `find transitive dependendencies also via type alias`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage12.Target::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage12.ClassUsingTarget::class
            )
        )
    }

    @Test
    fun `find transitive dependendencies also to an enum`() {
        // arrange
        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage13.TargetIsAnEnum::class

        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
            setOf(
                de.accso.dependencyanalyzer.testset.testpackage13.ClassUsingEnum::class,
                de.accso.dependencyanalyzer.testset.testpackage13.ClassUsingEnumValue::class
            )
        )
    }

    @Test
    fun `find all transitive dependency chains in the correct order`() {
        // arrange
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage14.Bottom::class

        // act
        val dependencyChainsOn = sut.transitiveDependencyChainsOn(targetKClazz)
        // dependentClazzes.forEach{ println(it.from.qualifiedName) }

        // assert
        assertThat(dependencyChainsOn).isEqualTo(
                setOf(
                        DependencyChain(Middle1::class,
                                emptyList(),
                                targetKClazz),
                        DependencyChain(Middle2::class,
                                emptyList(),
                                targetKClazz),
                        DependencyChain(Top1::class,
                                listOf(Middle1::class),
                                targetKClazz),
                        DependencyChain(Top1::class,
                                listOf(Middle2::class),
                                targetKClazz),
                        DependencyChain(Top2::class,
                                listOf(Middle1::class),
                                targetKClazz),
                        DependencyChain(Top2::class,
                                listOf(Middle2::class),
                                targetKClazz),
                )
        )
    }
}
