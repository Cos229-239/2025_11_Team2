plugins {
    alias(libs.plugins.android.application)   // Android application plugin
    alias(libs.plugins.kotlin.android)        // Kotlin Android support
    id("kotlin-kapt")                         // Annotation processing (needed for Room + Hilt)
    alias(libs.plugins.kotlin.compose)        // Compose compiler plugin
    id("dagger.hilt.android.plugin")          // Hilt plugin
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
    }
}

dependencies {
    // Core + lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("com.google.code.gson:gson:2.10.1")


    // Compose BOM ensures consistent versions
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.runtime)

    implementation(libs.androidx.compose.material3)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.5")
    implementation(libs.androidx.navigation.runtime.ktx)

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.foundation)
    implementation(libs.ui)
    implementation(libs.androidx.material3)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Kotlin stdlib
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")

    // Accompanist
    implementation("com.google.accompanist:accompanist-flowlayout:0.30.1")

    // Material icons (extended set)
    implementation("androidx.compose.material:material-icons-extended")

    // Hilt (DI)
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
