plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    androidTarget()
    jvm("desktop") {
        jvmToolchain(17)
    }
    sourceSets {
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
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.lifecycle.viewmodel.compose)
            }
        }
        named("desktopMain") {
            dependencies {

            }
        }
    }
}

android {
    namespace = "github.leavesczy.compose_tetris.android"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}