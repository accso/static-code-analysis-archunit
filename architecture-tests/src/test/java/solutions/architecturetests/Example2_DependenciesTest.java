package solutions.architecturetests;

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
        // arrange, act, assert
        ArchRuleDefinition.classes()
                .that()
                .resideInAPackage("..common..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("..common..", "java..", "org..")
                .because("we want to manage dependencies explicitely")
                .check(classesFromLibraryExample);
    }

    // test fails
    @Test
    void test_classes_in_common_must_not_use_other_classes_except_standard_classes_with_evaluate() {
        // arrange
        ArchRule rule = ArchRuleDefinition.classes()
                .that()
                .resideInAPackage("..common..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("..common..", "java..", "org..")
                .because("we want to manage dependencies explicitely");

        // act
        EvaluationResult evaluationResult = rule
                .because("we want to manage dependencies explicitely")
                .evaluate(classesFromLibraryExample);

        // assert
        assertThat(evaluationResult.getFailureReport().getDetails())
                .describedAs("only allowed dependencies of common classes")
                .isEmpty();
    }
}
