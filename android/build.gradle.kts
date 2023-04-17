@file:Suppress("UnstableApiUsage")

import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

fun generateBuildTime(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
    return simpleDateFormat.format(Date())
}

android {
    compileSdk = 33
    buildToolsVersion = "33.0.2"
    defaultConfig {
        applicationId = "github.leavesczy.compose_tetris"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"
        applicationVariants.all {
            outputs.all {
                if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                    outputFileName =
                        "compose_tetris_${name}_versionCode_${versionCode}_versionName_${versionName}_${generateBuildTime()}.apk"
                }
            }
        }
    }
    signingConfigs {
        create("release") {
            storeFile = File(rootDir, "key.jks")
            keyAlias = "leavesCZY"
            storePassword = "123456"
            keyPassword = "123456"
            enableV1Signing = true
            enableV2Signing = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/*.properties",
                "META-INF/*.version",
                "META-INF/{AL2.0,LGPL2.1}",
                "META-INF/*.md",
                "META-INF/CHANGES",
                "DebugProbesKt.bin",
                "kotlin-tooling-metadata.json"
            )
        )
    }
    namespace = "github.leavesczy.compose_tetris"
}

dependencies {
    implementation(project(":common"))
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.activity:activity-compose:1.7.0")
}