package solutions.staticcodeanalysis;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("ArchUnit test fails because of an intentional violation: EntityDao as a super class is not a JpaRepository")
class Example3_DaosAreJpaRepositoriesRevisitedTest {

    private static final String PACKAGE_PREFIX = "de.accso.library";

    /**
     * example 3 revisited - library example - now retrieving classes from the ClassFileImporter and checking its results manually
     */

    // static code analysis / test fails because 'EntityDao' is a super class and is not a JpaRepository
    @Test
    void test_that_all_JpaRepositories_and_all_Daos_are_exactly_the_same_classes() {
        DescribedPredicate<JavaClass> predicateClassImplementsJpaRepository =
                new DescribedPredicate<>("class implements JpaRepository") {
                    @Override
                    public boolean test(JavaClass clazz) {
                        // return (JpaRepository.class.isAssignableFrom(clazz.reflect()));
                        return clazz.isAssignableTo(JpaRepository.class);
                    }
                };

        DescribedPredicate<JavaClass> predicateClassIsADao =
                new DescribedPredicate<>("class is a Dao") {
                    @Override
                    public boolean test(JavaClass clazz) {
                        return clazz.getSimpleName().toLowerCase().endsWith("dao");
                    }
                };

        // arrange
        JavaClasses javaClassesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

        // act
        JavaClasses allJpaRepositoryClasses = javaClassesFromLibraryExample.that(predicateClassImplementsJpaRepository);
        JavaClasses allDaoClasses = javaClassesFromLibraryExample.that(predicateClassIsADao);

        // assert
        assertThat(allJpaRepositoryClasses)
                .describedAs("all JpaRepositories and all Daos are exactly the same classes")
                .isEqualTo(allDaoClasses);
    }
}