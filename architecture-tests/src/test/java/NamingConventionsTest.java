import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public class NamingConventionsTest {

    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages("de.accso.library");

    @Test
    void testImplementationsMustResideInPackageImpl() {
        ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Impl")
                .should().resideInAPackage("..impl")
                .check(classesFromLibraryExample);
    }

    @Test
    void teste_dass_jeder_Dao_ein_JpaRepository_ist_und_nur_diese() {
        ArchRuleDefinition.classes()
                .that().areAssignableTo(JpaRepository.class)
                .should().haveSimpleNameEndingWith("Dao")
                .check(classesFromLibraryExample);
        ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Dao")
                .should().beAssignableTo(JpaRepository.class)
                .check(classesFromLibraryExample);
    }
















    // ----------------------------------------------------------------------------------------------------------



    void testImplementationsMustResideInPackageImpl_() {
        ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Impl")
                .should().resideInAPackage("..impl")
                .check(classesFromLibraryExample);
    }


}
