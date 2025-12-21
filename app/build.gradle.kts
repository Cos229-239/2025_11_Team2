

plugins {
    alias(libs.plugins.android.application)   // Android application plugin
    alias(libs.plugins.kotlin.android)        // Kotlin Android support
    id("kotlin-kapt")                         // Annotation processing (needed for Room + Hilt)
    alias(libs.plugins.kotlin.compose)        // Compose compiler plugin
    id("dagger.hilt.android.plugin")          // Hilt plugin
    id("com.google.gms.google-services")     // Google services plugin

}

android {
    namespace = "com.reclaim.reclaim"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.reclaim.reclaim"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Room annotation processor options
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    buildFeatures { compose = true }

    composeOptions { kotlinCompilerExtensionVersion = "1.5.8" }

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
    }

    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "1.9"
        freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    }
}

dependencies {
    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    // WorkManager for background tasks
    implementation(libs.androidx.work.runtime.ktx)

    // --- Compose Dependencies ---
    implementation(platform(libs.androidx.compose.bom))

    // Use the newly defined alias for DataStore
    implementation(libs.androidx.datastore.preferences)

    // ... other Compose libraries
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended") // For extra icons

    // Glance for App Widgets
    implementation(libs.androidx.glance.appwidget)

    // Add this line for Accompanist Flow Layout
    implementation(libs.accompanist.flowlayout)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt for Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Firebase
    // Import the Firebase BOM to manage Firebase library versions
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    // Other essential libraries
    implementation(libs.coil.compose)
    implementation(libs.gson)

    // We are removing Room for now to eliminate it as a source of crashes.
    // If you need it for other features, we can add it back later.
     implementation(libs.androidx.room.runtime)
     implementation(libs.androidx.room.ktx)
     kapt(libs.androidx.room.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
