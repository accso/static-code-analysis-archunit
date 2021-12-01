package examples.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Test;

public class Example3_CycleDetectionTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 3 - library example - check for cycles
     */

//TODO zu hoeherwertige Konzepte verschieben

    // test fails with two cycles
    @Test
    void test_first_level_packages_must_be_free_of_cycles() {

        // TODO - live coding example 3

    }

    // test fails with several cycles
    @Test
    void test_all_packages_must_be_free_of_cycles() {

        // TODO - live coding example 3

    }
}
