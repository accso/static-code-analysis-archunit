package architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public class Example2_DependenciesTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 2 - library example - check dependencies on the library example
     */

    // test fails
    @Test
    void test_classes_in_common_must_not_use_other_classes_except_standard_classes() {
        // arrange, act, assert
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
    void test_allowed_dependencies_of_model_classes() {
        // arrange, act, assert
        ArchRuleDefinition.classes().that()
                .resideInAPackage("..model..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("java..", "javax..",
                        "..model..", "org.apache.commons.lang3..")
                .check(classesFromLibraryExample);
    }
}
