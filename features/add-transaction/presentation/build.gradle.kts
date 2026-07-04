plugins {
    id("convention-android-library")
    alias(deps.plugins.google.ksp)
}
android {
    namespace = "dev.esbi.mizan.transactions.add"
}
dependencies {
    implementation(projects.domain)

    // Coroutines (pure Kotlin, no Android)
    implementation(libs.kotlinx.coroutines.core)

    // MVIKotlin (pure Kotlin, KMP-ready)
    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.coroutines)

    // javax.inject for @Inject annotations (pure Java/Kotlin, no Android)
    implementation(libs.dagger.core)

    implementation(projects.core.mviKotlin)
    implementation(projects.core.dagger)
}
