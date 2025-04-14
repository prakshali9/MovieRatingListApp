plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.movieratinglistapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.movieratinglistapp"
        minSdk = 33
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
}

dependencies {
    // Firebase BoM (use only one version declaration)
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))

    // Firebase dependencies (no version needed because BoM handles it)
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")

    // AndroidX & Material
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("com.google.android.material:material:1.9.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    // Use kapt for annotation processors in Kotlin DSL

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation(libs.ext.junit)

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
