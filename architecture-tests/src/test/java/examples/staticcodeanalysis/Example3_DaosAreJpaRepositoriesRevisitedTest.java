package examples.staticcodeanalysis;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.assertj.core.api.Assertions.assertThat;

class Example3_DaosAreJpaRepositoriesRevisitedTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";

    /**
     * example 3 revisited - library example - now retrieving classes from the ClassFileImporter and checking its results manually
     */

    // static code analysis / test fails because 'EntityDao' is a super class and is not a JpaRepository
    @Test
    void test_that_all_JpaRepositories_and_all_Daos_are_exactly_the_same_classes() {

        // TODO - example 3 revisited with static code analysis

    }
}
