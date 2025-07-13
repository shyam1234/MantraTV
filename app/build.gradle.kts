plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.malviya.mantra"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.malviya.mantra"
        minSdk = 24
        targetSdk = 35
        versionCode = 19
        versionName = "1.0.19"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    coreLibraryDesugaring( libs.desugar.jdk.libs)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.timber)

    // Google Play Services
    //implementation(libs.play.services.base)
    //implementation(libs.play.services.auth)

    // Firebase
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))
//    implementation(libs.google.firebase.analytics)
//    implementation(libs.firebase.database)
//    implementation(libs.firebase.firestore)
    implementation(libs.firebase.remote.config)

    // Image Loading
    implementation(libs.coil.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // For JUnit 4
    testImplementation(libs.junit)

// For Kotlin coroutines testing
    testImplementation(libs.kotlinx.coroutines.test)

// For StateFlow testing (Turbine is optional, but useful)
    testImplementation(libs.turbine)
}