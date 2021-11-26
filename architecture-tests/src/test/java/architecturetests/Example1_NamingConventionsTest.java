package architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public class Example1_NamingConventionsTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 1 - library example - test naming conventions on "impl" classes
     */

    // test fails as AuthorizationImpl is not in an impl package
    @Test
    void test_implementation_classes_must_reside_in_a_packaged_named_impl() {
        // arrange
        ArchRule rule = ArchRuleDefinition.classes()
                .that()
                .haveSimpleNameEndingWith("Impl")
                .should()
                .resideInAPackage("..impl");

        // act, assert
        rule.because("we want to enforce naming conventions")
            .check(classesFromLibraryExample);
    }
}
