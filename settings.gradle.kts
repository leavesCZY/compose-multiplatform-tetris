pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    plugins {
        val agpVersion = "8.1.0"
        val composeVersion = "1.4.3"
        val kotlinVersion = "1.9.0"
        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)
        id("org.jetbrains.compose").version(composeVersion)
        kotlin("android").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
    }
}

include(":common", ":android", ":desktop")