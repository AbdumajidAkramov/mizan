plugins {
    id("convention-android-library")
    alias(deps.plugins.google.ksp)
}
android {
    namespace = "dev.esbi.mizan.ocr.scanner"
}
dependencies {

    // Coroutines (pure Kotlin, no Android)
    implementation(libs.kotlinx.coroutines.core)

    // javax.inject for @Inject annotations (pure Java/Kotlin, no Android)
    implementation(libs.dagger.core)
}
