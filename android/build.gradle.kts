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

fun getExtString(key: String): String {
    return project.ext[key] as String
}

fun getExtInt(key: String): Int {
    return Integer.parseInt(project.ext[key] as String)
}

android {
    compileSdk = getExtInt("compileSdkVersionExt")
    buildToolsVersion = getExtString("buildToolsVersionExt")
    defaultConfig {
        applicationId = getExtString("applicationIdExt")
        minSdk = getExtInt("minSdkVersionExt")
        targetSdk = getExtInt("targetSdkVersionExt")
        versionCode = getExtInt("versionCodeExt")
        versionName = getExtString("versionNameExt")
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
            storeFile = File(rootDir, ".\\key.jks")
            keyAlias = getExtString("keyAliasExt")
            storePassword = getExtString("storePasswordExt")
            keyPassword = getExtString("keyPasswordExt")
            enableV1Signing = true
            enableV2Signing = true
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = kotlin.run {
            val args = mutableListOf<String>()
            args.addAll(freeCompilerArgs)
            args.addAll(
                listOf(
                    "-Xjvm-default=all",
                    "-Xallow-jvm-ir-dependencies",
                    "-Xskip-prerelease-check",
                    "-Xopt-in=kotlin.RequiresOptIn",
                    "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-Xuse-experimental=androidx.compose.animation.ExperimentalAnimationApi",
                    "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
                    "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                    "-Xopt-in=com.google.accompanist.insets.ExperimentalAnimatedInsets",
                    "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi",
                    "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                )
            )
            args
        }
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
}

dependencies {
    implementation(project(":common"))
    implementation("com.google.android.material:material:1.6.0-alpha01")
}