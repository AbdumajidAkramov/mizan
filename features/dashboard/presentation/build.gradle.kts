plugins {
    id("convention-android-library")
    alias(deps.plugins.compose.compiler)
    alias(deps.plugins.google.ksp)
}
android {
    namespace = "dev.esbi.mizan.dashboard.presentation"
    buildFeatures {
        compose = true
    }
}
dependencies {
    implementation(projects.core.design)
    implementation(projects.features.addTransaction.domain)

    // Coroutines (pure Kotlin, no Android)
    implementation(deps.kotlinx.coroutines.core)

    // MVIKotlin (pure Kotlin, KMP-ready)
    implementation(deps.decompose.core)
    implementation(deps.decompose.compose)
    implementation(deps.decompose.android)
    implementation(platform(deps.compose.bom))
    implementation(deps.bundles.compose)

    // javax.inject for @Inject annotations (pure Java/Kotlin, no Android)
    implementation(deps.dagger.core)
}
