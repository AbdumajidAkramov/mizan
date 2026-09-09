plugins {
    id("convention-android-library")
    alias(deps.plugins.kotlin.serialization)
    alias(deps.plugins.google.ksp)
}

android {
    namespace = "dev.esbi.mizan.core"
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    // Room
    api(deps.androidx.room.runtime)
    api(deps.androidx.room.ktx)
    ksp(deps.androidx.room.compiler)

    // Coroutines
    api(deps.kotlinx.coroutines.core)
    api(deps.kotlinx.coroutines.android)

    // Dagger
    api(deps.dagger.core)

    // MVIKotlin
    api(deps.mvikotlin.core)
    api(deps.mvikotlin.main)
    api(deps.mvikotlin.coroutines)

    // Serialization
    api(deps.kotlinx.serialization.json)

    // Gson (Room TypeConverters)
    implementation(deps.gson)
}
