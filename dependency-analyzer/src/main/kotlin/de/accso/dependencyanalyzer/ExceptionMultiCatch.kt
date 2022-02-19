package de.accso.dependencyanalyzer

import kotlin.reflect.KClass

fun <T>(() -> T).catchAll(vararg caughtExceptions: KClass<out Throwable>,
                          catchBlock: (Throwable) -> T): T =
    try {
        this()
    }
    catch (ex: Throwable) {
        if (ex::class in caughtExceptions)
            catchBlock(ex)
        else
            throw ex
    }
