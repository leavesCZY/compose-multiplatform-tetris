import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0"
    id("com.android.library")
}

kotlin {
    android()
    jvm("desktop")
    sourceSets {
        named("commonMain") {
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
            }
        }
        named("androidMain") {
            dependencies {
                implementation("androidx.appcompat:appcompat:1.4.1")
                api("androidx.activity:activity-compose:1.4.0")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")
                implementation("com.google.accompanist:accompanist-insets:0.24.0-alpha")
                implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.0-alpha")
            }
        }
        named("desktopMain") {
            dependencies {
                implementation("javazoom:jlayer:1.0.1")
            }
        }
    }
}

android {
    compileSdk = Integer.parseInt(project.ext["compileSdkVersionExt"] as String)
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
    defaultConfig {
        minSdk = Integer.parseInt(project.ext["minSdkVersionExt"] as String)
        targetSdk = Integer.parseInt(project.ext["targetSdkVersionExt"] as String)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}