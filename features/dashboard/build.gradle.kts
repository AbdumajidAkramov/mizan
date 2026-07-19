plugins {
    id("convention-android-library")
    alias(deps.plugins.compose.compiler)
    alias(deps.plugins.google.ksp)
}

android {
    namespace = "dev.esbi.mizan.features.dashboard"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.ui)

    implementation(platform(deps.compose.bom))
    implementation(deps.bundles.compose)
    implementation(deps.bundles.decompose)
    implementation(deps.dagger.core)
    implementation(deps.kotlinx.coroutines.core)
}
