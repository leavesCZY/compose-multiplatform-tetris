import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.compose.compiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
    jvm("desktop")
    sourceSets {
        val desktopMain by getting
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
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
                    outputFileName = "compose-multiplatform-tetris-android.apk"
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
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

enum class OS(val id: String) {
    Linux("linux"),
    Windows("windows"),
    MacOS("macos")
}

val currentOS: OS by lazy {
    val os = System.getProperty("os.name")
    when {
        os.equals("Mac OS X", ignoreCase = true) -> OS.MacOS
        os.startsWith("Win", ignoreCase = true) -> OS.Windows
        os.startsWith("Linux", ignoreCase = true) -> OS.Linux
        else -> error("Unknown OS name: $os")
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        val mPackageName = "compose-multiplatform-tetris"
        nativeDistributions {
            includeAllModules = false
            modules = arrayListOf("java.desktop")
            when (currentOS) {
                OS.Windows -> {
                    targetFormats(TargetFormat.AppImage, TargetFormat.Exe, TargetFormat.Msi)
                }

                OS.MacOS -> {
                    targetFormats(TargetFormat.Dmg, TargetFormat.Pkg)
                }

                OS.Linux -> {
                    targetFormats(TargetFormat.Deb, TargetFormat.Rpm)
                }
            }
            packageName = mPackageName
            packageVersion = "1.0.0"
            description = mPackageName
            copyright = "© 2024 leavesCZY. All rights reserved."
            vendor = "leavesCZY"
            val resourcesDir = project.file("../composeApp/src/desktopMain/resources")
            windows {
                menuGroup = packageName
                dirChooser = true
                perUserInstall = true
                shortcut = true
                menu = true
                upgradeUuid = "4932C697-C420-4993-8A48-AEDA854A8895"
                iconFile.set(resourcesDir.resolve("windows_launch_icon.ico"))
                installationPath = packageName
            }
            macOS {
                bundleID = mPackageName
                setDockNameSameAsPackageName = true
                appStore = true
                iconFile.set(resourcesDir.resolve("macos_launch_icon.icns"))
            }
            linux {
                shortcut = true
                menuGroup = packageName
                iconFile.set(resourcesDir.resolve("linux_launch_icon.png"))
            }
        }
        buildTypes.release {
            proguard {
                isEnabled.set(true)
                obfuscate.set(true)
                optimize.set(true)
                joinOutputJars.set(true)
                configurationFiles.from("proguard-rules.pro")
            }
        }
    }
}