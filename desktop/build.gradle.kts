import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose")
    kotlin("multiplatform")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
            }
        }
        named("jvmTest") {

        }
    }
}

compose.desktop {
    application {
        mainClass = "github.leavesczy.compose_tetris.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "compose_tetris"
            packageVersion = "1.0.0"
            description = "Compose Tetris"
            val iconsRoot = project.file("../common/src/desktopMain/resources/images")
            windows {
                iconFile.set(iconsRoot.resolve("icon_compose_tetris.ico"))
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