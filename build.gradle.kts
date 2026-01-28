plugins {
    alias(libs.plugins.android.application).apply(apply = false)
    alias(libs.plugins.android.kotlin.multiplatform.library).apply(apply = false)
    alias(libs.plugins.jetbrains.kotlin.android).apply(apply = false)
    alias(libs.plugins.jetbrains.kotlin.multiplatform).apply(apply = false)
    alias(libs.plugins.jetbrains.compose.multiplatform).apply(apply = false)
    alias(libs.plugins.jetbrains.compose.compiler).apply(apply = false)
}