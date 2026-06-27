@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    versionCatalogs {
        create("deps") {
            from(files("../../gradle/deps.versions.toml"))
        }

        if (rootProject.name.contains(other = "build-logic")) {
            create("libs") {
                from(files("../../gradle/libs.versions.toml"))
            }
        }
    }

    repositories {
        google()
        mavenCentral()
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
        maven { url = uri("https://artifactory.aigroup.uz:443/artifactory/myid") }

        exclusiveContent {
            forRepository {
                maven { url = uri("https://jcenter.bintray.com/") }
            }
            filter {
                includeModule("com.github.Android-library-copy-dependencies", "SwipeRevealLayout")
                includeModule("com.github.blongho", "worldCountryData")
                includeModule("com.github.dhaval2404", "imagepicker")
                includeModule("com.github.florent37", "singledateandtimepicker")
                includeModule("com.github.jetradarmobile", "android-snowfall")
                includeModule("com.github.kenglxn.QRGen", "android")
                includeModule("com.github.kenglxn.QRGen", "core")
                includeModule("com.github.kenglxn.QRGen", "qrgen-parent")
                includeModule("com.github.shts", "StoriesProgressView")
                includeModule("com.github.thijsk", "TouchImageView")
                includeModule("com.github.yalantis", "ucrop")
                includeModule("com.github.yarolegovich", "DiscreteScrollView")
                includeModule("com.github.yuriy-budiyev", "code-scanner")
                includeModule("com.github.Zhuinden", "livedata-combinetuple-kt")
                includeModule("com.github.Zhuinden", "tuples-kt")
//                includeModule("com.redmadrobot", "input-mask-android")
                includeModule("io.fotoapparat.fotoapparat", "adapter-rxjava")
                includeModule("io.fotoapparat.fotoapparat", "fotoapparat")
                includeModule("com.github.blongh", "worldCountryData")
            }
        }
        maven { url = uri("https://mvnrepository.com/artifact/") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://github.com/mhiew/AndroidPdfViewer") }
        flatDir {
            dirs("libraries") // .jar fayl joylashgan papkani ko‘rsatamiz
        }
    }
}
