package solutions.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public class Example4_DaosAreJpaRepositoriesTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 4a and 4b - library example - test that all Daos are JpaRepositories (and vice versa)
     */

    // test fails because 'EntityDao' is a super class and is not a JpaRepository
    @Test
    void test_each_dao_is_a_jparepository_and_only_the_daos() {
        // arrange, act, assert
        ArchRuleDefinition.classes()
                .that().areAssignableTo(JpaRepository.class)
                .should().haveSimpleNameEndingWith("Dao")
                .because("each class which is a JpaRepository should be named *Dao")
                .check(classesFromLibraryExample);

        // arrange, act, assert
        ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Dao")
                .should().beAssignableTo(JpaRepository.class)
                .because("each class which is named *Dao should implement JpaRepository")
                .check(classesFromLibraryExample);
    }
}