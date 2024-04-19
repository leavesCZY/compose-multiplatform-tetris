import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    jvm("desktop")
    sourceSets {
        val desktopMain by getting
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.google.material)
            implementation(compose.materialIconsExtended)
        }
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.materialIconsExtended)
        }
    }
}

android {
    namespace = "github.leavesczy.compose_tetris"
    compileSdk = 34
    buildToolsVersion = "34.0.0"
    defaultConfig {
        applicationId = "github.leavesczy.compose_tetris"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        applicationVariants.all {
            outputs.all {
                if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                    outputFileName =
                        "compose_tetris_${name}_${versionName}_${versionCode}_${getApkBuildTime()}.apk"
                }
            }
        }
    }
    signingConfigs {
        create("release") {
            storeFile =
                File(project.rootProject.rootDir.absolutePath + File.separator + "doc" + File.separator + "key.jks")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources.excludes += setOf(
            "META-INF/*.properties",
            "META-INF/*.version",
            "META-INF/{AL2.0,LGPL2.1}",
            "META-INF/*.md",
            "META-INF/CHANGES",
            "DebugProbesKt.bin",
            "kotlin-tooling-metadata.json"
        )
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "github.leavesczy.compose_tetris"
            packageVersion = "1.0.0"
            description = "Compose Tetris"
            windows {
                val iconsRoot = project.file("../composeApp/src/desktopMain/resources")
                iconFile.set(iconsRoot.resolve("compose_tetris.ico"))
                menuGroup = "Compose Tetris"
                upgradeUuid = "18159995-d967-4CD2-8885-77BFA97CFA9F"
            }
        }
        buildTypes.release {
            proguard {
                configurationFiles.from("compose-desktop.pro")
                isEnabled.set(true)
                obfuscate.set(true)
                optimize.set(true)
            }
        }
    }
}

fun getTime(pattern: String): String {
    val simpleDateFormat = SimpleDateFormat(pattern)
    simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Shanghai")
    val time = Calendar.getInstance().time
    return simpleDateFormat.format(time)
}

fun getApkBuildTime(): String {
    return getTime(pattern = "yyyy_MM_dd_HH_mm_ss")
}