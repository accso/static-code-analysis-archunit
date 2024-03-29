package solutions.staticcodeanalysis

import de.accso.dependencyanalyzer.DependencyAnalyzer
import de.accso.dependencyanalyzer.DependencyChain
import de.accso.library.datamanagement.manager.BookDao
import de.accso.library.datamanagement.model.Customer
import de.accso.library.datamanagement.model.CustomerAccounting
import de.accso.library.datamanagement.model.MediaType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Example6_TransitiveDependenciesTest {

    /**
     * example 6 - library example - Kotlin - get transitive dependencies and dependency chains
     */
    private val TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED = "de.accso.library"

    // TODO live coding - example 6 (transitive dependencies)

    @Test
    fun `find all transitive dependencies to class Book in datamanagement`() {
        // arrange
        val dependencyAnalyzer = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val bookClazz = de.accso.library.datamanagement.model.Book::class

        // act
        val dependentClazzes = dependencyAnalyzer.clazzesTransitivelyDependentOn(bookClazz,
                "de.accso.library.datamanagement..")
        dependentClazzes.forEach{ println(it.qualifiedName) }

        // assert
        assertThat(dependentClazzes)
                .describedAs("transitive dependencies to class Book in datamanagement is fixed")
                .isEqualTo(setOf(
                    de.accso.library.datamanagement.manager.BookDao::class,
                    de.accso.library.datamanagement.model.MediaType::class,
                    de.accso.library.datamanagement.model.CustomerAccounting::class,
                    de.accso.library.datamanagement.model.Customer::class
                ))
    }

    @Test
    fun `find all transitive dependency chains to class Book in datamanagement`() {
        // arrange
        val dependencyAnalyzer = DependencyAnalyzer(TESTSET_PACKAGE_PREFIX_TO_BE_ANALYZED, true)

        val bookClazz = de.accso.library.datamanagement.model.Book::class

        // act
        val dependencyChainsOn = dependencyAnalyzer.transitiveDependencyChainsOn(bookClazz,
                "de.accso.library.datamanagement..")
        dependencyChainsOn.forEach{ println(it) }

        // assert
        assertThat(dependencyChainsOn)
                .describedAs("transitive dependency chains to class Book in datamanagement is fixed")
                .isEqualTo(setOf(
                    DependencyChain(BookDao::class, emptyList(), bookClazz),
                    DependencyChain(MediaType::class, emptyList(), bookClazz),
                    DependencyChain(CustomerAccounting::class, listOf(MediaType::class), bookClazz),
                    DependencyChain(Customer::class, listOf(CustomerAccounting::class, MediaType::class), bookClazz)
                ))
    }
}