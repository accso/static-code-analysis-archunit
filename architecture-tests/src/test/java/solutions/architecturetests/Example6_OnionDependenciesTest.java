package solutions.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

class Layer {
    String name;
    String pkg;
    Layer(String name, String pkg) { this.name = name; this.pkg = pkg; }
}

class Component {
    String name;
    Component(String name) { this.name = name; }
}


public class Example6_OnionDependenciesTest {

    private static final String PACKAGE_PREFIX = "de.accso.ecommerce";
    private static JavaClasses classesFromEcommerceExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 6 - ecommerce example - testing dependencies on components, on on onion architecture (via layers)
     */

    @Test
    void test_slices_are_free_of_cycles() {
        // arrange, act, assert
        SlicesRuleDefinition.slices()
                .matching(PACKAGE_PREFIX + ".(*)..")
                .should()
                .beFreeOfCycles()
                .because("cycles are bad")
                .check(classesFromEcommerceExample);
    }

    @Test
    void test_component_have_defined_dependencies() {
        // arrange
        Component billing = new Component("..billing..");
        Component common = new Component("..common..");
        Component sales = new Component("..sales..");
        Component shipping = new Component("..shipping..");
        Component warehouse = new Component("..warehouse..");

        // act, assert
        ArchRuleDefinition.classes()
                .that().resideInAnyPackage(billing.name)
                .should().onlyDependOnClassesThat().resideInAnyPackage(billing.name, common.name, shipping.name, "java..")
                .because("we want to manage dependencies from component billing explicitely")
                .check(classesFromEcommerceExample);
        ArchRuleDefinition.classes()
                .that().resideInAnyPackage(sales.name)
                .should().onlyDependOnClassesThat().resideInAnyPackage(sales.name, common.name, "java..")
                .because("we want to manage dependencies from component sales explicitely")
                .check(classesFromEcommerceExample);
        ArchRuleDefinition.classes()
                .that().resideInAnyPackage(shipping.name)
                .should().onlyDependOnClassesThat().resideInAnyPackage(shipping.name, common.name, sales.name, "java..")
                .because("we want to manage dependencies from component shipping explicitely")
                .check(classesFromEcommerceExample);
        ArchRuleDefinition.classes()
                .that().resideInAnyPackage(warehouse.name)
                .should().onlyDependOnClassesThat().resideInAnyPackage(warehouse.name, common.name, "java..")
                .because("we want to manage dependencies from component warehouse explicitely")
                .check(classesFromEcommerceExample);
        ArchRuleDefinition.classes()
                .that().resideInAnyPackage(common.name)
                .should().onlyDependOnClassesThat().resideInAnyPackage(common.name, "java..")
                .because("we want to manage dependencies from component common explicitely")
                .check(classesFromEcommerceExample);
    }

    @Test
    void test_onion_architecture_inside_one_component_using_layers() {
        // arrange
        Layer apiLayer = new Layer("API", "..api..");
        Layer applicationLayer = new Layer("Application", "..application..");
        Layer domainLayer = new Layer("Domain", "..domain..");
        Layer infrastructureLayer = new Layer("Infrastructure", "..infrastructure..");
        Layer uiLayer = new Layer("UI", "..ui..");

        Architectures.LayeredArchitecture layeredArchitecture = Architectures.layeredArchitecture()
                .withOptionalLayers(true)
                .layer(apiLayer.name).definedBy(apiLayer.pkg)
                .layer(applicationLayer.name).definedBy(applicationLayer.pkg)
                .layer(domainLayer.name).definedBy(domainLayer.pkg)
                .layer(infrastructureLayer.name).definedBy(infrastructureLayer.pkg)
                .layer(uiLayer.name).definedBy(uiLayer.pkg);

        // act, assert
        layeredArchitecture
                .whereLayer(apiLayer.name).mayOnlyBeAccessedByLayers(applicationLayer.name)
                .whereLayer(domainLayer.name).mayOnlyBeAccessedByLayers(applicationLayer.name)
                .whereLayer(infrastructureLayer.name).mayNotBeAccessedByAnyLayer()
                .whereLayer(uiLayer.name).mayNotBeAccessedByAnyLayer()
                .because("we want to enforce the onion architecure inside each component")
                .check(classesFromEcommerceExample);
    }

    @Test
    void test_onion_architecture_inside_one_component_using_onion() {
        // arrange, act, assert
        onionArchitecture()
                .withOptionalLayers(true)
                .domainModels(PACKAGE_PREFIX + ".core.domain.model..")
                .domainServices(PACKAGE_PREFIX + ".core.domain.services..")
                .applicationServices(PACKAGE_PREFIX + ".core.application..")
                .adapter("persistence", PACKAGE_PREFIX + ".infrastructure.persistence..")
                .adapter("cli", PACKAGE_PREFIX + ".infrastructure.cli..")
                .adapter("monitoring", PACKAGE_PREFIX + ".infrastructure.monitoring..")
                .because("we want to enforce the onion architecure inside each component")
                .check(classesFromEcommerceExample);
    }
}
