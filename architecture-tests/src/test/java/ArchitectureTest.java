import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Test;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class ArchitectureTest {

    private JavaClasses classes = new ClassFileImporter().importPackages("de.accso.library");


    @Test
    void testClassesInCommonMustNotUseOtherClassesExceptStandardClasses() {
         classes().that().resideInAPackage("..common..")
                .should().onlyDependOnClassesThat().resideInAnyPackage("..common..", "java..", "org..")
                 .check(classes);
    }


    /**
     * Freeze Rule für Annotationen
     */
    @Test
    void testEntityClassesHaveToBeAnnotatedAsSuch() {

       ArchRule annotationRule = classes().that().resideInAPackage("..model..")
               .and().areNotEnums()
               .and().areNotInnerClasses()
               .should().beAnnotatedWith(Entity.class)
               //  .orShould().beAnnotatedWith(Embeddable.class)
               ;

       //annotationRule.check(classes);

       ArchRule freezeRule = FreezingArchRule.freeze(
              annotationRule);
       freezeRule.check(classes);

       // Check der gleichen Annotations-Regel, aber ohne Freeze
       // annotationRule.check(classes);
    }

    @Test
    void testThatLibraryUtilClassesDependOnlyContainJavaStandardClasses() {

        ArchRule dependRule = classes().that()
                .resideInAPackage("..util..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("java..") ;

        dependRule.check(classes);
    }

    @Test
    void testCorrectDependenciesOfLibraryModelClasses() {
        classes().that()
                .resideInAPackage("..model..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("java..", "javax..",
                        "..model..", "org.apache.commons.lang3..")
                .check(classes);
    }
}