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
    fun `findet transitiv alle abhaengigen API-Klassen auch in mehreren Dateien und auch bei mehrfacher Abhaengigkeit`() {
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
    fun `findet transitiv alle abhaengigen Domain-Klassen auch in mehreren Dateien und auch bei mehrfacher Abhaengigkeit`() {
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
    fun `filtert exakt ueber den Packagenamen und filtert also Subpackage aus`() {
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
    fun `filtert alles da Filter-Package-Namen nicht existieren`(nonExistingPackage: String) {
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
    fun `filtert alles da Filter-Package-Name keine eingehende Abhaengigkeit auf Target haben kann`(filterForPackage: String) {
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
    fun `wirft Exception da zu analysierende Klasse nicht gefunden wird`(analyzeNonExistingPackagePrefix: String) {
        // arrange
        val sut = DependencyAnalyzer(analyzeNonExistingPackagePrefix, true)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage1.TargetUsedFromMultipleFilesInAPI::class

        // act & assert
        assertThrows<IllegalArgumentException> {
            sut.clazzesTransitivelyDependentOn(targetKClazz)
        }
    }

    @Test
    fun `wirft Exception da zu analysierende Test-Klasse nicht gefunden wird wenn Test-Code nicht analysiert wird`() {
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
    fun `findet transitiv alle abhaengigen Domain-Klassen die von einer Klasse inklusive der Kt-Datei`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten auch bei Nullability`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten auf eine Sealed-Class inklusive aller Subsealed-Classes`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten auf eine Sealed-Sub-Class`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten auch ueber den Generic Type in einer Collection`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten auch aus einem Companion Object`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten per Vererbung von Super-Klassen`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten auf ein Interface`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten aus einem Object`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten sowohl auf Data Class als auch auf normale Class`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten auch aus einem Array oder List oder Set`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten auch bei Type-Alias`() {
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
    fun `findet transitiv alle eingehenden Abhaengigkeiten auch auf ein Enum`() {
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
    fun `setzt die Abhaengigkeiten in einer korrekten Kette ein`() {
        // arrange
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val targetKClazz = de.accso.dependencyanalyzer.testset.testpackage14.Bottom::class

        // act
        val dependencyChainsOn = sut.dependencyChainsOn(targetKClazz)
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
