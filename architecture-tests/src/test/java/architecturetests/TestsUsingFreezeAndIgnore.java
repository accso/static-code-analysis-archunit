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
     * Beispiel 7 ignore
     */

    @Test
    void testImplementationsMustResideInPackageImplIgnoreAuthorization() {
        ArchRuleDefinition.classes()
                .that()
                .haveSimpleNameEndingWith("Impl")
                .should()
                .resideInAPackage("..impl")
                .check(classesFromLibraryExample);
    }

    /**
     * Beispiel 8 freeze
     */
    @Test
    void testImplementationsMustResideInPackageImplWithFreezing(){

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
