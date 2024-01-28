package solutions.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

@Disabled("ArchUnit test fails because of an intentional violation: AuthorizationImpl is neither ignored in the ignore_patterns file nor frozen")
public class Example5_TestsUsingFreezeAndIgnore {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 5 - library example - test by ignoring some classes - see globally active archunit_ignore_patterns.txt
     */

    // test fails

    @Test
    void test_each_dao_is_a_jparepository_ignore_entitydao() {
        ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Dao")
                .should().beAssignableTo(JpaRepository.class)
                .check(classesFromLibraryExample);
    }

    /**
     * example 5 - library example - freeze violations which should be ignored
     */
    @Test
    void test_each_dao_is_a_jparepository_with_freezing() {
        FreezingArchRule.freeze(
                        ArchRuleDefinition.classes()
                                .that().haveSimpleNameEndingWith("Dao")
                                .should().beAssignableTo(JpaRepository.class)
        )
        .check(classesFromLibraryExample);
    }
}
