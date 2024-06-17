plugins {
    id("com.android.application")
}

android {
    namespace = "android.scheduler.johnsalazar"
    compileSdk = 34

    defaultConfig {
        applicationId = "android.scheduler.johnsalazar"
        minSdk = 27
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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.appcompat:appcompat:$rootProject.appCompatVersion")



    // Room components
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    androidTestImplementation("androidx.room:room-testing:2.6.1")

//    // Lifecycle components
//    implementation("androidx.lifecycle:lifecycle-viewmodel:$rootProject.lifecycleVersion")
//    implementation("androidx.lifecycle:lifecycle-livedata:$rootProject.lifecycleVersion")
//    implementation("androidx.lifecycle:lifecycle-common-java8:$rootProject.lifecycleVersion")
//
//    // UI
//    implementation("androidx.constraintlayout:constraintlayout:$rootProject.constraintLayoutVersion")
//    implementation("com.google.android.material:material:$rootProject.materialVersion")

    // Testing
//    testImplementation("junit:junit:$rootProject.junitVersion")
//    androidTestImplementation("androidx.arch.core:core-testing:$rootProject.coreTestingVersion")
//    androidTestImplementation("androidx.test.espresso:espresso-core:$rootProject.espressoVersion" {
//        exclude( group: 'com.android.support', module: "support-annotations")
//    })

    androidTestImplementation("androidx.test.ext:junit:$rootProject.androidxJunitVersion")
}