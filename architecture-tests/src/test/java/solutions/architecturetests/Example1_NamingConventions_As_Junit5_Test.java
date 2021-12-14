package solutions.architecturetests;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.CacheMode;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "de.accso.library", cacheMode = CacheMode.FOREVER)
public class Example1_NamingConventions_As_Junit5_Test {

    /**
     * example 1 - library example - test naming conventions on "impl" classes, written as a Junit 5 test
     */

    // test fails as AuthorizationImpl is not in an impl package
    @ArchIgnore(reason = "ArchUnit test fails because of an intentional violation: AuthorizationImpl is not in an impl package")
    @ArchTest
    public static final ArchRule test_implementation_classes_must_reside_in_a_package_named_impl =
        ArchRuleDefinition.classes()
                .that()
                .haveSimpleNameEndingWith("Impl")
                .should()
                .resideInAPackage("..impl");

    // test fails as AuthorizationImpl is not in an impl package
    @ArchIgnore(reason = "ArchUnit test fails because of an intentional violation: AuthorizationImpl is not in an impl package")
    @ArchTest()
    public static final ArchRule test_implementation_classes_must_not_reside_outside_a_package_named_impl =
            ArchRuleDefinition.noClasses()
                .that()
                .haveSimpleNameEndingWith("Impl")
                .should()
                .resideOutsideOfPackage("..impl..");
}
