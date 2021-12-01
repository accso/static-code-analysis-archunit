package examples.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Example2_DependenciesTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 2 - library example - check dependencies on the library example
     */

    // test fails
    @Test
    void test_classes_in_common_must_not_use_other_classes_except_standard_classes() {

        // TODO - live coding example 2

    }

    // test fails
    @Test
    void test_classes_in_common_must_not_use_other_classes_except_standard_classes_with_evaluate() {

        // TODO - live coding example 2

    }
}
