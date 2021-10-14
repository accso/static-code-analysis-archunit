import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private JavaClasses classes = new ClassFileImporter().importPackages("de.accso.library");

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