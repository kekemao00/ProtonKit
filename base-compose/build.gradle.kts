plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "me.kekemao.base_compose"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.kekemao.base_compose"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":base"))

    // Compose bom
    // https://developer.android.com/jetpack/compose/setup?hl=en
    api(platform(libs.androidx.compose.bom))
    androidTestApi(platform(libs.androidx.compose.bom))

    // Choose one of the following:
    // Material Design 3
    api(libs.androidx.compose.material3.material3)
    // or Material Design 2
//    implementation(libs.androidx.material)
    // or skip Material Design and build directly on top of foundational components
//    implementation(libs.foundation)
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
//    implementation(libs.androidx.compose.ui.ui)

    // Android Studio Preview support
    api(libs.androidx.compose.ui.ui.tooling.preview)
    debugApi(libs.androidx.compose.ui.ui.tooling)

    // UI Tests
    androidTestApi(libs.ui.test.junit4)
    debugApi(libs.ui.test.manifest)

//    // Optional - Included automatically by material, only add when you need
//    // the icons but not the material library (e.g. when using Material3 or a
//    // custom design system based on Foundation)
//    api(libs.androidx.material.icons.core)
//    // Optional - Add full set of material icons
//    api(libs.androidx.material.icons.extended)
//    // Optional - Add window size utils
//    api(libs.androidx.material3.window.size)

//    // Optional - Integration with activities
//    api(libs.androidx.activity.compose)
//    // Optional - Integration with ViewModels
//    api(libs.androidx.lifecycle.viewmodel.compose)
//    // Optional - Integration with LiveData
//    api(libs.androidx.runtime.livedata)
//    // Optional - Integration with RxJava
//    api(libs.androidx.runtime.rxjava2)


}