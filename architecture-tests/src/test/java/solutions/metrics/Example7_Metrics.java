package solutions.metrics;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.metrics.ArchitectureMetrics;
import com.tngtech.archunit.library.metrics.LakosMetrics;
import com.tngtech.archunit.library.metrics.MetricsComponents;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class Example7_Metrics {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * example 8 - ecommerce example - show some cumulative dependency metrics
     */

    @Test
    void test_cumulative_dependency_metrics() {
        // arrange
        Set<JavaPackage> packages = classesFromLibraryExample.getPackage("de.accso.library").getSubpackages();

        // act
        MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(packages);
        LakosMetrics allMetrics = ArchitectureMetrics.lakosMetrics(components);

        // The sum of all dependsOn values of all components
        int     ccd = allMetrics.getCumulativeComponentDependency();
        // The CCD divided by the number of all components
        double  acd = allMetrics.getAverageComponentDependency();
        // The ACD divided by the number of all components
        double racd = allMetrics.getRelativeAverageComponentDependency();
        // The CCD of the system divided by the CCD of a balanced binary tree with the same number of components
        double nccd = allMetrics.getNormalizedCumulativeComponentDependency();

        // assert
        assertThat( ccd).isEqualTo(28);
        assertThat( acd).isEqualTo(3.5f);
        assertThat(racd).isEqualTo(0.4375f);
        assertThat(nccd).isGreaterThan(1.3f);
    }
}
