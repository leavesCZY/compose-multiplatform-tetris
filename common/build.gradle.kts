plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
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
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        named("commonMain") {
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
                implementation("androidx.appcompat:appcompat:1.6.0")
                api("androidx.activity:activity-compose:1.6.1")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
                val accompanistVersion = "0.28.0"
                implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
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
    compileSdk = 33
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
    }
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    namespace = "github.leavesczy.compose_tetris.android"
}