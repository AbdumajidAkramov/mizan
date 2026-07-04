plugins {
    id("convention-android-library")
    alias(deps.plugins.google.ksp)
}
android {
    namespace = "dev.esbi.mizan.voice.recognition"
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
    // javax.inject for @Inject annotations (pure Java/Kotlin, no Android)
    implementation(libs.dagger.core)
}
