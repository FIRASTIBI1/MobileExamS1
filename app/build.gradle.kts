plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplicationjdid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplicationjdid"
        minSdk = 29
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

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation ("com.ramotion.circlemenu:circle-menu:0.3.2")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.activity:activity:1.9.2")
    implementation ("com.google.firebase:firebase-messaging:23.2.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation("org.apache.poi:poi-ooxml:5.2.3")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}