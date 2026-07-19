plugins {
    id("convention-android-library")
    alias(deps.plugins.google.ksp)
}

android {
    namespace = "dev.esbi.mizan.addtransaction.data"
}


dependencies {
    implementation(projects.features.addTransaction.domain)

    // Coroutines
    implementation(deps.kotlinx.coroutines.core)
    implementation(deps.kotlinx.coroutines.android)

    // Dagger2 (for @Inject annotations on RepositoryImpl and Seeder)
    implementation(deps.dagger.core)

    // Gson (for Room TypeConverters)
    implementation(deps.gson)
}
