plugins {
    id("convention-android-library")
    alias(deps.plugins.google.ksp)
}
android {
    namespace = "dev.esbi.mizan.dashboard"

    buildFeatures {
        compose = true
    }
}
dependencies {
    implementation(deps.bundles.compose)
    implementation(deps.bundles.decompose)
}
