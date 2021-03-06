package examples.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Class is a live coding template, therefore empty")
public class Example1_NamingConventionsTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 1 - library example - test naming conventions on "impl" classes
     */

    // test fails as AuthorizationImpl is not in an impl package
    @Test
    void test_implementation_classes_must_reside_in_a_package_named_impl() {

        // TODO live coding - example 1 (impl classes in impl package)

    }

    // test fails as AuthorizationImpl is not in an impl package
    @Test
    void test_implementation_classes_must_not_reside_outside_a_package_named_impl() {

        // TODO live coding - example 1 (no impl classes outside impl package)

    }
}
