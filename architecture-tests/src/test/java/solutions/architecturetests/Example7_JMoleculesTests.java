package solutions.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.jmolecules.archunit.JMoleculesArchitectureRules;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Test using JMolecules fails because of an intentional violation: Wrong layering in the eCommerce example")
public class Example7_JMoleculesTests {
    private static final String PACKAGE_PREFIX = "de.accso.ecommerce.";
    private static JavaClasses classesFromEcommerceExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    // test fails because wrong the layering in the eCommerce example does not fulfill the JMolecules layer rules

    @Test
    void testLayersUsingJMolecules() {
       ArchRule rule = JMoleculesArchitectureRules.ensureLayering();
       rule.check(classesFromEcommerceExample);
    }
}
