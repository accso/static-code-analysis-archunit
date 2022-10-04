package de.accso.dependencyanalyzer

import com.tngtech.archunit.core.domain.PackageMatcher
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import kotlin.reflect.KClass

fun Set<JavaClass>.alreadyAnalyzed(clazz: JavaClass) =
       this.containsClazzName(clazz.fullName)
    || this.containsClazzNameBeforeFirstDollar(clazz.fullName)

fun Set<JavaClass>.containsClazzName(clazzName: String) =
        this.map { it.fullName }.any { it == clazzName }

fun Set<JavaClass>.containsClazzNameBeforeFirstDollar(clazzName: String) =
        this.map { it.fullName }.any { it.startsWith(clazzName.substringBefore("\$")) } // first occurence

fun JavaClass.residesInAnyOfThesesPackages(packageNames: List<String>) =
        packageNames.any { this.residesInPackage(it) }

fun JavaClass.residesInPackage(packageName: String) =
        PackageMatcher.of(packageName).matches(this.packageName)

fun JavaClass.isEqualToKClazz(kClazz: KClass<*>): Boolean =
        this.fullName == kClazz.qualifiedName
    ||  this.fullName == kClazz.qualifiedName.replaceLastDotByDollar() // needed for sub classes of a sealed class

fun JavaClass.toClazz(): Class<*> = this.reflect()

fun JavaClass.toKClazz(): KClass<*> = this.toClazz().kotlin

fun JavaClasses.toKClazzes(): Set<KClass<*>> = this.map { it.toKClazz() }.toSet()

// ------------------------------------------------------------------------------------------------------------

private fun String?.replaceLastDotByDollar(): String? {
    if (this == null) return null
    val index = this.lastIndexOf(".")
    return this.substring(0, index) + "$" + this.substring(index + 1)
}

