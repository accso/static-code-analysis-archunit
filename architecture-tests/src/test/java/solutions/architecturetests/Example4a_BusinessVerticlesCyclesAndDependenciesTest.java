package solutions.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Disabled("ArchUnit test fails because of an intentional violation: The eCommerce example has cycles in slices and also some wrong dependencies")
public class Example4a_BusinessVerticlesCyclesAndDependenciesTest {

    private static final String PACKAGE_PREFIX = "de.accso.ecommerce.";
    private static JavaClasses classesFromEcommerceExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 4a - ecommerce example - testing cycles, dependencies on business verticles (aka slices)
     */

    // TODO live coding - example 4a (ecommerce, cycles, component dependencies)

    // test fails because of sales->shipping->sales cycle
    @Test
    void test_slices_are_free_of_cycles() {
        // arrange, act, assert
        SlicesRuleDefinition.slices()
                .matching(PACKAGE_PREFIX + "(*)..")
                .should()
                .beFreeOfCycles()
                .because("cycles are bad")
                .check(classesFromEcommerceExample);
    }

    // ---------------------------------------------------------------------------------------------

    class BusinessVerticle {
        String name;
        String pkg;
        BusinessVerticle(String name, String pkg) { this.name = name; this.pkg = pkg; }
    }

    // test fails because of wrong dependencies in SalesEventProducer to ShippingMessaging (instead of SalesMessaging)
    @Test
    void test_component_have_defined_dependencies() {
        // arrange
        BusinessVerticle billing   = new BusinessVerticle("Billing",   "..billing..");
        BusinessVerticle common    = new BusinessVerticle("Common",    "..common..");
        BusinessVerticle sales     = new BusinessVerticle("Sales",     "..sales..");
        BusinessVerticle shipping  = new BusinessVerticle("Shipping",  "..shipping..");
        BusinessVerticle warehouse = new BusinessVerticle("Warehouse", "..warehouse..");

        // act and assert
        checkDependencies(billing,   common, shipping, sales);
        checkDependencies(sales,     common, warehouse);
        checkDependencies(shipping,  common, sales);
        checkDependencies(warehouse, common);
        checkDependencies(common);
    }
    private void checkDependencies(BusinessVerticle from, BusinessVerticle... to) {
        List<String> toPackages = Arrays.stream(to).map(c -> c.pkg).collect(Collectors.toList());
        toPackages.add("java..");  // add dependency to java as default
        toPackages.add(from.pkg);  // add self as allowed dependency
        String[] toPackagesWithJava = toPackages.toArray(String[]::new);

        ArchRuleDefinition.classes()
                .that().resideInAnyPackage(from.pkg)
                .should().onlyDependOnClassesThat().resideInAnyPackage(toPackagesWithJava)
                .because("we want to manage dependencies from business verticle " + from.name + " explicitely")
                .check(classesFromEcommerceExample);
    }
}
