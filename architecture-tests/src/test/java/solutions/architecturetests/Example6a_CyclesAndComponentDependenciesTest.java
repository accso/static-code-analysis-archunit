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
public class Example6a_CyclesAndComponentDependenciesTest {

    private static final String PACKAGE_PREFIX = "de.accso.ecommerce.";
    private static JavaClasses classesFromEcommerceExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 6a - ecommerce example - testing cycles, dependencies on components
     */

    // TODO live coding - example 6a

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

    class Component {
        String name;
        String pkg;
        Component(String name, String pkg) { this.name = name; this.pkg = pkg; }
    }

    // test fails because of wrong dependencies in SalesEventProducer to ShippingMessaging (instead of SalesMessaging)
    @Test
    void test_component_have_defined_dependencies() {
        // arrange
        Component billing   = new Component("Billing",   "..billing..");
        Component common    = new Component("Common",    "..common..");
        Component sales     = new Component("Sales",     "..sales..");
        Component shipping  = new Component("Shipping",  "..shipping..");
        Component warehouse = new Component("Warehouse", "..warehouse..");

        // act and assert
        checkDependencies(billing,   common, shipping, sales);
        checkDependencies(sales,     common, warehouse);
        checkDependencies(shipping,  common, sales);
        checkDependencies(warehouse, common);
        checkDependencies(common);
    }
    private void checkDependencies(Component from, Component... to) {
        List<String> toPackages = Arrays.stream(to).map(c -> c.pkg).collect(Collectors.toList());
        toPackages.add("java..");  // add depdendency to java as default
        toPackages.add(from.pkg); // add self as allowed dependency
        String[] toPackagesWithJava = toPackages.toArray(String[]::new);

        ArchRuleDefinition.classes()
                .that().resideInAnyPackage(from.pkg)
                .should().onlyDependOnClassesThat().resideInAnyPackage(toPackagesWithJava)
                .because("we want to manage dependencies from component " + from.name + " explicitely")
                .check(classesFromEcommerceExample);
    }
}
