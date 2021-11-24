package staticcodeanalysis;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;

class DaosAreJpaRepositories {

    private static final String PACKAGE_PREFIX = "de.accso.library";
    private static JavaClasses classesFromLibraryExample = new ClassFileImporter().importPackages(PACKAGE_PREFIX);

    /**
     * Beispiel 4 a und b, revisited
     */

    @Test
    void finde_alle_Daos_und_alle_JpaRepositories_und_teste_auf_gleichheit() {
        DescribedPredicate<JavaClass> predicateImplementsJpaRepository =
                new DescribedPredicate<>("implements JpaRepository") {
                    @Override
                    public boolean apply(JavaClass clazz) {
                        return (JpaRepository.class.isAssignableFrom(clazz.reflect()));
                    }
                };

        JavaClasses allJpaRepositoryImplementors =
                new ClassFileImporter().importPackages(PACKAGE_PREFIX).that(predicateImplementsJpaRepository);
        for (JavaClass clazz: allJpaRepositoryImplementors) {
            System.out.println(clazz.getFullName());
        }

        ArchRuleDefinition.classes().that()
                .resideInAPackage("..model..")
                .should()
                .haveSimpleNameEndingWith("Dao")
                .check(allJpaRepositoryImplementors);
    }
}