package solutions.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

public class Example7_TestsUsingFreezeAndIgnore {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 7a - library example - test by ignoring some classes - see globally active archunit_ignore_patterns.txt
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
     * example 7b - library example - freeze violations which should be ignored
     */
    @Test
    void test_implementation_classes_must_reside_in_a_package_named_impl_with_freezing() {
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
