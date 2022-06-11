import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.CacheMode;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "de.accso.archunitsonar", cacheMode = CacheMode.FOREVER)
public class ArchUnitDependencyTest {

    @ArchTest
    public static final ArchRule test_dependencies =
            ArchRuleDefinition.noClasses()
                    .that()
                    .haveSimpleNameEndingWith("MyMath")
                    .should()
                    .dependOnClassesThat()
                    .haveSimpleNameEndingWith("MyString");
}
