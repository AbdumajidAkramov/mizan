plugins {
    id("convention-android-library")
    alias(deps.plugins.google.ksp)
}

android {
    namespace = "dev.esbi.mizan.data"
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(projects.domain)

    // Room
    implementation(deps.androidx.room.runtime)
    implementation(deps.androidx.room.ktx)
    ksp(deps.androidx.room.compiler)

    // Coroutines
    implementation(deps.kotlinx.coroutines.core)
    implementation(deps.kotlinx.coroutines.android)

    // Dagger2 (for @Inject annotations on RepositoryImpl and Seeder)
    implementation(libs.dagger.core)

    // Gson (for Room TypeConverters)
    implementation(deps.gson)
}
