package architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Test;

public class TestsUsingFreezeAndIgnore {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 7 - library example - test by ignoring some classes - see archunit_ignore_patterns.txt - works globally
     */

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
     * example 8 - library example - freeze violations which should be ignored
     */
    @Test
    void testImplementationsMustResideInPackageImplWithFreezing() {
//        FreezingArchRule.freeze(
                ArchRuleDefinition.classes()
                        .that()
                        .haveSimpleNameEndingWith("Impl")
                        .should()
                        .resideInAPackage("..impl")
//        )
        .check(classesFromLibraryExample);
    }
}
