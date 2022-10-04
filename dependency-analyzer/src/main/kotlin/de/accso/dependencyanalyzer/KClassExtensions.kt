package de.accso.dependencyanalyzer

import com.tngtech.archunit.core.domain.PackageMatcher
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import kotlin.reflect.KClass
import kotlin.reflect.jvm.javaType

fun KClass<*>.isConcreteClass() = !isAbstract && !isSealed

fun KClass<*>.clazzAndItsSealedSubClazzes() = listOf(this) + this.sealedSubclasses


fun KClass<*>.residesInAnyPackage(packageNames: List<String>) =
        packageNames.any { this.residesInPackage(it) }

fun KClass<*>.residesInPackage(packageName: String) =
        if (this.qualifiedName == null)
            false
        else {
            PackageMatcher.of(packageName).matches(this.packageName())
        }

fun KClass<*>?.packageName() = this?.qualifiedName?.substringBeforeLast("." + this.simpleName)


//TODO optimize, cache the result (potentialy together with "alreadyAnalyzed? flag")
fun KClass<*>.usesAsArgumentInConstructor(targetKClazz: KClass<*>): Boolean =
        {
            val typeOftargetKClazz = targetKClazz.java

            this.constructors.any { constructor ->
                constructor.parameters.any { parameter ->
                    parameter.type.javaType.isEqualTo(typeOftargetKClazz)
                 || parameter.type.javaType.usesAsGenericType(typeOftargetKClazz)
                }
            }
        }
        .catchAll(UnsupportedOperationException::class,
                  InternalError::class,
                  ClassNotFoundException::class,
                  NoClassDefFoundError::class,
                  IncompatibleClassChangeError::class)
        {
            // ignore errors of this type:
            //   java.lang.UnsupportedOperationException: Packages and file facades are not yet supported in Kotlin reflection.
            //   java.lang.InternalError: Enclosing method not found
            //   java.lang.ClassNotFoundException
            false
        }


fun KClass<*>.getName() = when {
    this.qualifiedName != null -> this.qualifiedName
    this.simpleName != null -> this.simpleName
    else -> this.toString()
}?.replaceFirst("class ", "")


fun KClass<*>.throwExceptionBecauseJavaClazzNotFoundForKClazz(analyzeDependenciesOnPackagesWithPrefix: String) {
    val toKClazzPackageName = this.packageName()

    throw if (toKClazzPackageName != null && !toKClazzPackageName.startsWith(analyzeDependenciesOnPackagesWithPrefix)) {
        IllegalArgumentException(
            "Analysis of dependencies to target clazz ${this.qualifiedName} " +
            "not possible, as this clazz cannot be found in the list of clazzes to be analyzed. " +
            "Cause: Clazz' package name '${toKClazzPackageName}' is not prefixed with '${analyzeDependenciesOnPackagesWithPrefix}'"
        )
    } else {
        IllegalArgumentException(
            "Analysis of dependencies to target clazz ${this.qualifiedName} " +
            "not possible, as this clazz cannot be found in the list of clazzes to be analyzed. " +
            "Is the clazz' package name '${toKClazzPackageName}' really be prefixed with '${analyzeDependenciesOnPackagesWithPrefix}'? " +
            "And/or: Is the classpath specified correctly?"
        )
    }
}

fun KClass<*>.toJavaClazz(allJavaClasses: JavaClasses): JavaClass? = allJavaClasses.firstOrNull { it.isEqualToKClazz(this) }
