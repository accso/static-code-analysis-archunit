package architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public class NamingConventionsTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * Beispiel 1
     */

    @Test
    void testImplementationsMustResideInPackageImpl() {
        ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Impl")
                .should().resideInAPackage("..impl")
                .check(classesFromLibraryExample);
    }
}
