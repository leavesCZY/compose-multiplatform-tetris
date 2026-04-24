import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose.multiplatform)
    alias(libs.plugins.jetbrains.compose.compiler)
}

kotlin {
    jvm(name = "desktop")
    jvmToolchain(jdkVersion = 21)
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.value(JvmTarget.JVM_21)
        }
    }
    android {
        namespace = "github.leavesczy.compose_tetris.base"
        compileSdk {
            version = release(version = 36)
        }
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
        androidResources {
            enable = true
        }
    }
    sourceSets {
        androidMain {
            dependencies {

            }
        }
        commonMain {
            dependencies {
                implementation(libs.compose.material3)
                implementation(libs.compose.lifecycle.viewmodel)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.material.icons.extended)
            }
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

enum class OS {
    Linux,
    Windows,
    MacOS;
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

compose {
    resources {
        publicResClass = false
        packageOfResClass = "github.leavesczy.compose_tetris.resources"
        generateResClass = auto
    }
    desktop {
        application {
            mainClass = "github.leavesczy.compose_tetris.MainKt"
            val mPackageName = "compose-multiplatform-tetris"
            nativeDistributions {
                includeAllModules = false
                modules = arrayListOf("java.desktop")
                when (currentOS) {
                    OS.Windows -> {
                        targetFormats(TargetFormat.AppImage, TargetFormat.Exe)
                    }

                    OS.MacOS -> {
                        targetFormats(TargetFormat.Dmg)
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
}