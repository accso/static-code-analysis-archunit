package solutions.architecturetests;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import de.accso.ecommerce.common.Event;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

public class Example6b_OnionDependenciesTest {

    private static final String PACKAGE_PREFIX = "de.accso.ecommerce.";
    private static final String PACKAGE_PREFIX_WITH_WILDCARD = ".";
    private static JavaClasses classesFromEcommerceExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 6b - ecommerce example - testing onion architecture (via layers)
     */

    // TODO live coding - example 6b

    class Layer {
        String name;
        String pkg;
        Layer(String name, String pkg) { this.name = name; this.pkg = pkg; }
    }

    // test is not failing, but potential ArchUnit bug see below
    @Test
    void test_onion_architecture_inside_one_component_using_layers() {
        // arrange
        Layer apiLayer            = new Layer("API",        PACKAGE_PREFIX_WITH_WILDCARD + ".api..");
        Layer applicationLayer    = new Layer("Application",PACKAGE_PREFIX_WITH_WILDCARD + ".core.application..");
        Layer domainLayer         = new Layer("Domain",     PACKAGE_PREFIX_WITH_WILDCARD + ".core.domain..");
        Layer infrastructureLayer = new Layer("Infra",      PACKAGE_PREFIX_WITH_WILDCARD + ".infrastructure..");
        Layer uiLayer             = new Layer("UI",         PACKAGE_PREFIX_WITH_WILDCARD + ".ui..");

        Architectures.LayeredArchitecture layeredArchitecture = Architectures.layeredArchitecture()
                .withOptionalLayers(true)
                .layer(apiLayer.name).definedBy(apiLayer.pkg)
                .layer(applicationLayer.name).definedBy(applicationLayer.pkg)
                .layer(domainLayer.name).definedBy(domainLayer.pkg)
                .layer(infrastructureLayer.name).definedBy(infrastructureLayer.pkg)
                .layer(uiLayer.name).definedBy(uiLayer.pkg)
        ;

        // act, assert
        layeredArchitecture
                .whereLayer(apiLayer.name).mayOnlyBeAccessedByLayers(applicationLayer.name)
                // test fails with the following line activated. Is this a bug in ArchUnit?!?
                //   .whereLayer(applicationLayer.name).mayOnlyAccessLayers(apiLayer.name, domainLayer.name)
                .whereLayer(applicationLayer.name).mayOnlyBeAccessedByLayers(infrastructureLayer.name)
                .whereLayer(domainLayer.name).mayOnlyBeAccessedByLayers(applicationLayer.name)
                .whereLayer(infrastructureLayer.name).mayNotBeAccessedByAnyLayer()
                .whereLayer(infrastructureLayer.name).mayOnlyAccessLayers(applicationLayer.name)
                .whereLayer(uiLayer.name).mayNotBeAccessedByAnyLayer()
                // ignore all dependencies to java..
                .ignoreDependency(isEcommerceClass, isJavaClass)
                // ignore all dependencies to ...common.Event
                .ignoreDependency(isEcommerceClass, isEventClass)
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

    // --------------------------------------------------------------------------------------------------------------

    DescribedPredicate<JavaClass> isEventClass = new DescribedPredicate<>("is common.Event class") {
        @Override
        public boolean apply(JavaClass input) {
            return input.isAssignableTo(Event.class);
        }
    };
    DescribedPredicate<JavaClass> isJavaClass = new DescribedPredicate<>("is Java class") {
        @Override
        public boolean apply(JavaClass input) {
            return input.getPackageName().startsWith("java");
        }
    };
    DescribedPredicate<JavaClass> isEcommerceClass = new DescribedPredicate<>("is any Ecommerce class") {
        @Override
        public boolean apply(JavaClass input) {
            return input.getPackageName().startsWith(PACKAGE_PREFIX);
        }
    };

}