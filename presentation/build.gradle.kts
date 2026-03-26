plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(project(":domain"))

    // Coroutines (pure Kotlin, no Android)
    implementation(libs.kotlinx.coroutines.core)

    // MVIKotlin (pure Kotlin, KMP-ready)
    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.coroutines)

    // javax.inject for @Inject annotations (pure Java/Kotlin, no Android)
    implementation(libs.dagger.core)
}
