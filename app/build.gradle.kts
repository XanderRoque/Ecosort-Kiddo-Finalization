plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.ecosort.ecosortkiddo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ecosort.ecosortkiddo"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("androidx.sqlite:sqlite:2.4.0")

    //Card View Independency
    implementation ("androidx.cardview:cardview:1.0.0")

    //Blurry Carview
    //implementation ("jp.wasabeef:blurry:4.0.0")

    // Responsive size support
    //implementation ("com.intuit.sdp:sdp-android:1.1.0")
    //Constraint
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    //@dimen/_40sdp
    implementation ("com.intuit.sdp:sdp-android:1.1.1")
    //Material
    //implementation ("com.google.android.material:material:1.9.0")


    //Additional Dependencies in Responsive



//    implementation ("com.example.library:version")

}