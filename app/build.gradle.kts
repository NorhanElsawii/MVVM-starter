plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.starter.mvvm"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    flavorDimensions += listOf(Config.DEFAULT)
    productFlavors {
        all {
            dimension = Config.DEFAULT
        }
        create("staging") {
            buildConfigField(Config.STRING_TYPE, Config.MAIN_HOST, "\"https://reqres.in/api/\"")
        }
        create("pre-live") {
            buildConfigField(Config.STRING_TYPE, Config.MAIN_HOST, "\"https://reqres.in/api/\"")
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
        }
        getByName("release") {
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

    dependencies {
        implementation("androidx.core:core-ktx:1.6.0")
        implementation("androidx.appcompat:appcompat:1.3.1")
        implementation("com.google.android.material:material:1.4.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.0")


        //testing
        testImplementation("junit:junit:4.+")
        androidTestImplementation("androidx.test.ext:junit:1.1.3")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0'")
    }
}

private object Config {
    const val STRING_TYPE = "String"
    const val MAIN_HOST = "MAIN_HOST"
    const val DEFAULT = "default"
}