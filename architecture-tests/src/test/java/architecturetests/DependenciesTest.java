package architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public class DependenciesTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 2 - library example - check dependencies on the library example
     */

    // test fails
    @Test
    void testClassesInCommonMustNotUseOtherClassesExceptStandardClasses() {
        ArchRuleDefinition.classes()
                .that()
                .resideInAPackage("..common..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("..common..", "java..", "org..")
                .check(classesFromLibraryExample);
    }

    // test fails
    @Test
    void testCorrectDependenciesOfLibraryModelClasses() {
        ArchRuleDefinition.classes().that()
                .resideInAPackage("..model..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("java..", "javax..",
                        "..model..", "org.apache.commons.lang3..")
                .check(classesFromLibraryExample);
    }
}
