import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose.compiler)
}

android {
    namespace = "github.leavesczy.compose_tetris"
    buildToolsVersion = "37.0.0"
    compileSdk {
        version = release(version = 36)
    }
    defaultConfig {
        applicationId = "github.leavesczy.compose_tetris"
        versionCode = 1
        versionName = "1.0.0"
        minSdk {
            version = release(version = 23)
        }
        targetSdk {
            version = release(version = 36)
        }
    }
    buildFeatures.apply {
        compose = true
    }
    val basePluginExtension = project.extensions.getByType(BasePluginExtension::class.java)
    basePluginExtension.apply {
        archivesName.set("compose-multiplatform-tetris-android")
    }
    signingConfigs {
        create("release") {
            storeFile = File(rootProject.rootDir.absolutePath, "key.jks")
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
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
            "**/*.kotlin_module",
            "**/CHANGES",
            "**/LICENSE.txt",
            "**/{AL2.0,LGPL2.1}",
            "**/DebugProbesKt.bin",
            "**/app-metadata.properties",
            "**/kotlin-tooling-metadata.json",
            "**/version-control-info.textproto",
            "**/androidsupportmultidexversion.txt",
        )
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.compose.material3)
    implementation(projects.composeApp)
}