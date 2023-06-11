plugins {
    val kotlinVersion = "1.8.20"
    val composeVersion = "1.4.0"
    val agpVersion = "8.0.2"
    kotlin("multiplatform").version(kotlinVersion).apply(false)
    kotlin("android").version(kotlinVersion).apply(false)
    id("org.jetbrains.compose").version(composeVersion).apply(false)
    id("com.android.application").version(agpVersion).apply(false)
    id("com.android.library").version(agpVersion).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}