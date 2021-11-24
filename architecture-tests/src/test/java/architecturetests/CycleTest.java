package architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Test;

public class CycleTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * Beispiel 3
     */

    @Test
    void testFirstLevelPackagesMustBeFreeOfCycles() {
        SlicesRuleDefinition.slices()
                .matching("de.accso.library.(*)..")
                .should()
                .beFreeOfCycles()
                .check(classesFromLibraryExample);
    }

    @Test
    void testAllPackagesMustBeFreeOfCycles() {
        SlicesRuleDefinition.slices()
                .matching("de.accso.library.(**)..")
                .should()
                .beFreeOfCycles()
                .check(classesFromLibraryExample);
    }

}
