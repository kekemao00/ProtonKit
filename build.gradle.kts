// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    // https://developer.android.com/develop/ui/compose/compiler?hl=zh-cn
    // https://developer.android.com/jetpack/androidx/releases/compose-kotlin?hl=zh-cn
    alias(libs.plugins.compose.compiler) apply false
}