package staticcodeanalysis

import de.accso.dependencyanalyzer.*
import de.accso.library.datamanagement.manager.BookDao
import de.accso.library.datamanagement.model.Customer
import de.accso.library.datamanagement.model.CustomerAccounting
import de.accso.library.datamanagement.model.MediaType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransitiveDependencies {
    /**
     * Example 5 - transitive dependencies, Kotlin unit tests
     */

    companion object {
        const val TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED = "de.accso.library"
        const val WILDCARD = ".."
    }

    @Test
    fun `findet alle transitiven Abhaengigkeiten zur Klasse Book in datamanagement-manager`() {
        // arrange
        val dependencyAnalyzer = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val bookClazz = de.accso.library.datamanagement.model.Book::class

        // act
        val dependentClazzes = dependencyAnalyzer.clazzesTransitivelyDependentOn(bookClazz, "de.accso.library.datamanagement.manager..")
        // dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes).isEqualTo(
                setOf(
                    de.accso.library.datamanagement.manager.BookDao::class
                )
        )
    }

    @Test
    fun `findet alle transitiven Abhaengigkeitsketten zur Klasse Book`() {
        // arrange
        val dependencyAnalyzer = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val bookClazz = de.accso.library.datamanagement.model.Book::class

        // act
        val dependencyChainsOn = dependencyAnalyzer.dependencyChainsOn(bookClazz, "de.accso.library.datamanagement..")
        dependencyChainsOn.forEach{ println(it) }

        // assert
        assertThat(dependencyChainsOn).isEqualTo(
                setOf(
                        DependencyChain(
                                BookDao::class,
                                emptyList(),
                                bookClazz),
                        DependencyChain(
                                MediaType::class,
                                emptyList(),
                                bookClazz),
                        DependencyChain(
                                CustomerAccounting::class,
                                listOf(MediaType::class),
                                bookClazz),
                        DependencyChain(
                                Customer::class,
                                listOf(CustomerAccounting::class, MediaType::class),
                                bookClazz)
                )
        )

    }
}