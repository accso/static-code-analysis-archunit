package examples.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Test;

public class Example4_TestsUsingFreezeAndIgnore {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 4 - library example - test by ignoring some classes - see globally active archunit_ignore_patterns.txt
     */

    @Test
    void test_implementation_classes_must_reside_in_a_packaged_named_impl_ignore_authorization() {

        // TODO - live coding example 4 (archunit_ignore_patterns.txt)

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

        // TODO - live coding example 4

        ArchRuleDefinition.classes()
                .that()
                .haveSimpleNameEndingWith("Impl")
                .should()
                .resideInAPackage("..impl")
                .check(classesFromLibraryExample);
    }
}
