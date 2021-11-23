import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

class ArchitectureTest {

    private JavaClasses classes = new ClassFileImporter().importPackages("de.accso.library");

    @Test
    void testClassesInCommonMustNotUseOtherClassesExceptStandardClasses() {
         ArchRuleDefinition.classes().that().resideInAPackage("..common..")
                .should().onlyDependOnClassesThat().resideInAnyPackage("..common..", "java..", "org..")
                .check(classes);
    }

    @Test
    void testEntityClassesHaveToBeAnnotatedAsSuch() {
        ArchRuleDefinition.classes().that().resideInAPackage("..model..")
                .and().areNotEnums()
                .and().areNotInnerClasses()
                .should().beAnnotatedWith(Entity.class)
              //  .orShould().beAnnotatedWith(Embeddable.class)
                .check(classes);
    }

    @Test
    void testThatLibraryUtilClassesDependOnlyContainJavaStandardClasses() {
        ArchRuleDefinition.classes().that()
                .resideInAPackage("..util..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("java..")
                .check(classes);
    }

    @Test
    void testCorrectDependenciesOfLibraryModelClasses() {
        ArchRuleDefinition.classes().that()
                .resideInAPackage("..model..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("java..", "javax..",
                        "..model..", "org.apache.commons.lang3..")
                .check(classes);
    }
}