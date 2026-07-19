plugins {
    id("convention-android-library")
    alias(deps.plugins.google.ksp)
}
android {
    namespace = "dev.esbi.mizan.voice.recognition"
}
dependencies {
    implementation(deps.kotlinx.coroutines.core)
    // javax.inject for @Inject annotations (pure Java/Kotlin, no Android)
    implementation(deps.dagger.core)
}
