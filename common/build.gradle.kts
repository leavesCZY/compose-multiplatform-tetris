import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0"
    id("com.android.library")
}

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val commonMain by getting {
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.appcompat:appcompat:1.4.1")
                api("androidx.activity:activity-compose:1.4.0")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")
                implementation("com.google.accompanist:accompanist-insets:0.22.0-rc")
                implementation("com.google.accompanist:accompanist-systemuicontroller:0.22.0-rc")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation("javazoom:jlayer:1.0.1")
            }
        }
        val desktopTest by getting
    }
}

android {
    compileSdk = Integer.parseInt(project.ext["compileSdkVersionExt"] as String)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDir("src/commonMain/res")
    defaultConfig {
        minSdk = Integer.parseInt(project.ext["minSdkVersionExt"] as String)
        targetSdk = Integer.parseInt(project.ext["targetSdkVersionExt"] as String)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}