package solutions.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

public class Example1_NamingConventionsTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 1 - library example - test naming conventions on "impl" classes
     */

    // test fails as AuthorizationImpl is not in an impl package
    @Test
    void test_implementation_classes_must_reside_in_a_package_named_impl() {
        // arrange, act, assert
       ArchRuleDefinition.classes()
                .that()
                .haveSimpleNameEndingWith("Impl")
                .should()
                .resideInAPackage("..impl")
                .check(classesFromLibraryExample);
    }

    // test fails as AuthorizationImpl is not in an impl package
    @Test
    void test_implementation_classes_must_not_reside_outside_a_package_named_impl() {
        // arrange, act, assert
        ArchRuleDefinition.noClasses()
                .that()
                .haveSimpleNameEndingWith("Impl")
                .should()
                .resideOutsideOfPackage("..impl")
                .because("we want to enforce naming conventions")
                .check(classesFromLibraryExample);
    }
}
