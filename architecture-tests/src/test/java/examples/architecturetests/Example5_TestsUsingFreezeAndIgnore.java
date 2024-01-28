package examples.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

@Disabled("Class is a live coding template, therefore empty")
public class Example5_TestsUsingFreezeAndIgnore {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 5 - library example - test by ignoring some classes - see globally active archunit_ignore_patterns.txt
     */

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
    void test_implementation_classes_must_reside_in_a_package_named_impl_with_freezing() {

        // TODO live coding - example 5 (ignore with freezing)

        ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Dao")
                .should().beAssignableTo(JpaRepository.class)
                .check(classesFromLibraryExample);
    }
}
