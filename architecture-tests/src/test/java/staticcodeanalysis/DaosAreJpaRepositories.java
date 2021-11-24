package staticcodeanalysis;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.assertj.core.api.Assertions.assertThat;

class DaosAreJpaRepositories {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * Example 4a and 4b, now retrieving classes from the ClassFileImporter and checking its results manually
     */

    @Test
    void test_that_all_JpaRepositories_and_all_Daos_are_exactly_the_same_classes() {
        DescribedPredicate<JavaClass> predicateClassImplementsJpaRepository =
                new DescribedPredicate<>("class implements JpaRepository") {
                    @Override
                    public boolean apply(JavaClass clazz) {
                        return clazz.isAssignableTo(JpaRepository.class);
                        // or:
                        // return (JpaRepository.class.isAssignableFrom(clazz.reflect()));
                    }
                };

        DescribedPredicate<JavaClass> predicateClassIsADao =
                new DescribedPredicate<>("class is a Dao") {
                    @Override
                    public boolean apply(JavaClass clazz) {
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
                .isEqualTo(allDaoClasses);
    }
}