plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

group = "com.kekemao00.protonkit.key-value-store"

android {
    namespace = "me.kekemao.keyvaluestore"
    compileSdk = 34

    defaultConfig {
        minSdk = 25

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":base"))
    // Preferences DataStore (SharedPreferences like APIs)
    // https://developer.android.com/topic/libraries/architecture/datastore?hl=zh-cn
    // https://developer.android.com/codelabs/basic-android-kotlin-training-preferences-datastore?hl=zh_cn#2
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.gradle.simple)

}
