package staticcodeanalysis

import com.tngtech.archunit.core.domain.JavaClass
import de.accso.dependencyanalyzer.*
import de.accso.library.datamanagement.manager.BookDao
import de.accso.library.datamanagement.model.Customer
import de.accso.library.datamanagement.model.CustomerAccounting
import de.accso.library.datamanagement.model.MediaType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TransitiveDependencies {
    companion object {
        const val TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED = "de.accso.library"
        const val WILDCARD = ".."
    }

    @Test
    fun `findet alle transitiven Abhaengigkeiten zur Klasse Book in datamanagement-manager`() {
        // arrange
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val bookClazz = de.accso.library.datamanagement.model.Book::class

        // act
        val dependentClazzes = sut.clazzesTransitivelyDependentOn(bookClazz, "de.accso.library.datamanagement.manager..")
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        Assertions.assertThat(dependentClazzes).isEqualTo(
                setOf(
                    de.accso.library.datamanagement.manager.BookDao::class
                )
        )
    }

    @Test
    fun `findet alle transitiven Abhaengigkeitsketten zur Klasse Book`() {
        // arrange
        val sut = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val bookClazz = de.accso.library.datamanagement.model.Book::class

        // act
        val dependencyChainsOn = sut.dependencyChainsOn(bookClazz, "de.accso.library.datamanagement..")
        // dependencyChainsOn.forEach{ println(it) }

        // assert
        Assertions.assertThat(dependencyChainsOn).isEqualTo(
                setOf(
                        DependencyChainForKClazz(
                                BookDao::class,
                                emptyList(),
                                bookClazz),
                        DependencyChainForKClazz(
                                MediaType::class,
                                emptyList(),
                                bookClazz),
                        DependencyChainForKClazz(
                                CustomerAccounting::class,
                                listOf(MediaType::class),
                                bookClazz),
                        DependencyChainForKClazz(
                                Customer::class,
                                listOf(CustomerAccounting::class, MediaType::class),
                                bookClazz)
                )
        )

    }
}