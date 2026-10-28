import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.ecloset"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    val localProperties = Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            file.inputStream().use { load(it) }
        }
    }

    defaultConfig {
        applicationId = "com.example.ecloset"
        minSdk = 36
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val baseUrl = System.getenv("BASE_URL")
            ?: localProperties.getProperty("BASE_URL")
            ?: ""
        val devUrl1 = System.getenv("DEV_URL_1")
            ?: localProperties.getProperty("DEV_URL_1")
            ?: ""
        val devUrl2 = System.getenv("DEV_URL_2")
            ?: localProperties.getProperty("DEV_URL_2")
            ?: ""
        buildConfigField( "String", "BASE_URL", "\"$baseUrl\"" )
        buildConfigField( "String", "DEV_URL_1", "\"$devUrl1\"" )
        buildConfigField( "String", "DEV_URL_2", "\"$devUrl2\"" )
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.foundation.layout)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.runtime)
	//implementation(libs.androidx.compose.runtime)
	implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
	implementation(libs.androidx.compose.ui.unit)
	implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.lifecycle.viewmodel.ktx)
	//implementation(libs.androidx.ui.graphics)
	//implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
	implementation(libs.androidx.ui.graphics)
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.converter.gson)
    implementation(libs.lottie.compose)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.moshi.kotlin)
    //implementation(libs.converter.moshi)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}