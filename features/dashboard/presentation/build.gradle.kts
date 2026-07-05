plugins {
    id("convention-android-library")
    alias(deps.plugins.google.ksp)
}
android {
    namespace = "dev.esbi.mizan.dashboard.presentation"
}
dependencies {
    implementation(projects.core.design)
    implementation(projects.features.addTransaction.domain)

    // Coroutines (pure Kotlin, no Android)
    implementation(libs.kotlinx.coroutines.core)

    // MVIKotlin (pure Kotlin, KMP-ready)
    implementation(deps.decompose.core)
    implementation(deps.decompose.compose)
    implementation(deps.decompose.android)
    implementation(deps.bundles.compose)

    // javax.inject for @Inject annotations (pure Java/Kotlin, no Android)
    implementation(libs.dagger.core)
}
