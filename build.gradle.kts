plugins {
    val agpVersion = "8.0.2"
    val composeVersion = "1.4.1"
    val kotlinVersion = "1.8.20"
    id("com.android.application").version(agpVersion).apply(false)
    id("com.android.library").version(agpVersion).apply(false)
    id("org.jetbrains.compose").version(composeVersion).apply(false)
    kotlin("android").version(kotlinVersion).apply(false)
    kotlin("multiplatform").version(kotlinVersion).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}