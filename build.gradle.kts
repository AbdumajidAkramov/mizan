// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(deps.plugins.android.application) apply false
    alias(deps.plugins.android.library) apply false
    alias(deps.plugins.kotlin.android) apply false
    alias(deps.plugins.kotlin.jvm) apply false
    alias(deps.plugins.compose.compiler) apply false
    alias(deps.plugins.google.services) apply false
    alias(deps.plugins.firebase.crashlytics) apply false
    alias(deps.plugins.room) apply false
}
