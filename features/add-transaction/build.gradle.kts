plugins {
    id("convention-android-library")
    alias(deps.plugins.google.ksp)
}
android {
    namespace = "dev.esbi.mizan.transactions"
}
dependencies {
    implementation(projects.features.addTransaction.domain)
    implementation(projects.features.addTransaction.data)
    implementation(projects.features.addTransaction.presentation)
}
