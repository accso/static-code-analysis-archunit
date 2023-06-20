package solutions.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("ArchUnit test fails because of an intentional violation: AuthorizationImpl is neither ignored in the ignore_patterns file nor frozen")
public class Example4_TestsUsingFreezeAndIgnore {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 4 - library example - test by ignoring some classes - see globally active archunit_ignore_patterns.txt
     */

    // test fails

    @Test
    void test_implementation_classes_must_reside_in_a_packaged_named_impl_ignore_authorization() {
        ArchRuleDefinition.classes()
                .that()
                .haveSimpleNameEndingWith("Impl")
                .should()
                .resideInAPackage("..impl")
                .check(classesFromLibraryExample);
    }

    /**
     * example 4 - library example - freeze violations which should be ignored
     */
    @Test
    void test_implementation_classes_must_reside_in_a_package_named_impl_with_freezing() {
        FreezingArchRule.freeze(
                ArchRuleDefinition.classes()
                        .that()
                        .haveSimpleNameEndingWith("Impl")
                        .should()
                        .resideInAPackage("..impl")
        )
        .check(classesFromLibraryExample);
    }
}
