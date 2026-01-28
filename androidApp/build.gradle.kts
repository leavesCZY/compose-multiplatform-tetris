import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.compose.compiler)
}

android {
    namespace = "github.leavesczy.compose_tetris"
    compileSdk = 36
    buildToolsVersion = "36.1.0"
    defaultConfig {
        applicationId = "github.leavesczy.compose_tetris"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
        applicationVariants.all {
            outputs.all {
                if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                    outputFileName = "compose-multiplatform-tetris-android.apk"
                }
            }
        }
    }
    buildFeatures.apply {
        compose = true
    }
    signingConfigs {
        create("release") {
            storeFile = File(project.rootProject.rootDir.absolutePath + File.separator + "key.jks")
            keyAlias = "leavesCZY"
            storePassword = "123456"
            keyPassword = "123456"
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
            )
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.value(JvmTarget.JVM_21)
            optIn.addAll(
                setOf(
                    "androidx.compose.foundation.layout.ExperimentalLayoutApi"
                )
            )
        }
    }
    packaging {
        resources.excludes += setOf(
            "**/*.md",
            "**/*.version",
            "**/*.properties",
            "**/LICENSE.txt",
            "**/DebugProbesKt.bin",
            "**/app-metadata.properties",
            "**/kotlin-tooling-metadata.json",
            "**/version-control-info.textproto",
            "**/androidsupportmultidexversion.txt",
            "META-INF/CHANGES",
            "META-INF/{AL2.0,LGPL2.1}",
            "META-INF/**/*.kotlin_module",
            "META-INF/version-control-info.textproto",
        )
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.material3)
    implementation(projects.composeApp)
}