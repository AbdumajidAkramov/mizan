import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        allWarningsAsErrors.set(false)
        apiVersion.set(KotlinVersion.KOTLIN_2_1) // 🔥 `KOTLIN_2_1` obyekt sifatida ishlatiladi
        languageVersion.set(KotlinVersion.KOTLIN_2_1)
        jvmTarget.set(JvmTarget.JVM_21)

        freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
            "-progressive",
            "-Xcontext-receivers",
            "-Xskip-prerelease-check"
        )
    }
}