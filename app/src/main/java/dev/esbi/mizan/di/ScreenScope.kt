package dev.esbi.mizan.di

import javax.inject.Scope

/**
 * Custom scope annotation for screen-level dependencies.
 * Dependencies annotated with this scope will live as long as the screen's subcomponent.
 * This ensures that each screen gets its own instance of scoped dependencies,
 * preventing issues like "Value is already initialized" with MVIKotlin Executors.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ScreenScope
