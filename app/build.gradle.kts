plugins {
    id("com.android.application")
    id ("kotlin-android")
}

android {
    namespace = "com.qw.adse"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.qw.adse"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 这里添加
        multiDexEnabled=true
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation("com.github.bumptech.glide:glide:4.11.0")

    implementation("com.github.goldze:MVVMHabit:4.0.0")


    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.android.volley:volley:1.1.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    //XUI
    implementation("com.github.xuexiangjys:XUI:1.2.1")
    implementation("me.imid.swipebacklayout.lib:library:1.1.0")

    implementation("com.github.mamumu:mmDialog:1.2.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.10")

    implementation("me.majiajie:pager-bottom-tab-strip:2.2.5") {
        exclude(group= ("com.android.support"))
    }

    implementation("com.github.li-xiaojun:XPopup:2.9.19"){
        exclude(group=("com.davemorrissey.labs"), module=("subsampling-scale-image-view-androidx"))
    }


    implementation("com.makeramen:roundedimageview:2.3.0")

    implementation("com.appsflyer:af-android-sdk:6.12.1")
    implementation("com.alibaba:fastjson:1.2.83")
    implementation("com.nostra13.universalimageloader:universal-image-loader:1.9.5")

}