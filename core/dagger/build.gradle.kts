plugins {
    alias(deps.plugins.kotlin.jvm)
}

dependencies {

    // Coroutines (pure Kotlin, no Android)
    implementation(deps.kotlinx.coroutines.core)

    // MVIKotlin (pure Kotlin, KMP-ready)
    implementation(deps.mvikotlin.core)
    implementation(deps.mvikotlin.main)
    implementation(deps.mvikotlin.coroutines)

    // javax.inject for @Inject annotations (pure Java/Kotlin, no Android)
    implementation(deps.dagger.core)
}
