package architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Test;

import javax.persistence.Entity;


//    TODO: raus damit, ist nur Schmierblatt

class ArchitectureTest {
//    private static final String PACKAGE_PREFIX = "de.accso.library";
//    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);
//
//    /**
//     * Freeze Rule für Annotationen
//     */
//    @Test
//    void testEntityClassesHaveToBeAnnotatedAsSuch() {
//
//       ArchRule annotationRule = ArchRuleDefinition.classes().that().resideInAPackage("..model..")
//               .and().areNotEnums()
//               .and().areNotInnerClasses()
//               .should().beAnnotatedWith(Entity.class)
//               //  .orShould().beAnnotatedWith(Embeddable.class)
//               ;
//
//       //annotationRule.check(classes);
//
//       ArchRule freezeRule = FreezingArchRule.freeze(
//              annotationRule);
//       freezeRule.check(classesFromLibraryExample);
//
//       // Check der gleichen Annotations-Regel, aber ohne Freeze
//       // annotationRule.check(classes);
//    }

}
