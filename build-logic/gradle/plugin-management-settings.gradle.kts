pluginManagement {
    repositories {
        exclusiveContent {
            forRepository {
                maven {
                    name = "huawei"
                    setUrl("https://developer.huawei.com/repo/")
                }
            }
            filter {
                includeGroupByRegex("com\\.huawei.*")
            }
        }
        exclusiveContent {
            forRepository {
                maven {
                    name = "r8-releases"
                    setUrl("https://storage.googleapis.com/r8-releases/raw")
                }
            }
            filter {
                includeModule("com.android.tools", "r8")
            }
        }
        maven {
            name = "google-android"
            setUrl("https://dl.google.com/dl/android/maven2/")
        }
        maven {
            name = "gradle-central-plugin"
            setUrl("https://plugins.gradle.org/m2")
        }
        maven {
            name = "maven-central"
            setUrl("https://repo1.maven.org/maven2")
        }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.toString() == "com.huawei.agconnect") {
                useModule("com.huawei.agconnect:agcp:${requested.version}")
            }
        }
    }
}
