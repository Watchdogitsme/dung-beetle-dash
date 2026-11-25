import java.io.File

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val releaseKeystorePath = System.getenv("ANDROID_KEYSTORE_FILE")?.takeIf { it.isNotBlank() }
val hasReleaseSigning = releaseKeystorePath != null && File(releaseKeystorePath).exists()

android {
    namespace = "io.github.watchdogitsme.dungbeetledash"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.watchdogitsme.dungbeetledash"
        minSdk = 24
        targetSdk = 35
        versionCode = (project.findProperty("versionCode") as String?)?.toIntOrNull() ?: 1
        versionName = (project.findProperty("versionName") as String?) ?: "1.0.0"
    }

    if (hasReleaseSigning) {
        signingConfigs {
            create("release") {
                storeFile = File(releaseKeystorePath!!)
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD") ?: ""
                keyAlias = System.getenv("ANDROID_KEY_ALIAS") ?: ""
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD") ?: ""
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets["main"].assets.srcDir(layout.buildDirectory.dir("generated/webAssets"))

    lint {
        abortOnError = true
        checkReleaseBuilds = false
    }
}

val syncWebGame by tasks.registering(Copy::class) {
    from(rootProject.layout.projectDirectory.file("../Beetal dash.html"))
    into(layout.buildDirectory.dir("generated/webAssets/www"))
    rename { "index.html" }
}

tasks.configureEach {
    if (
        name == "preBuild" ||
        (name.startsWith("merge") && name.endsWith("Assets")) ||
        (name.startsWith("generate") && name.contains("Lint")) ||
        name.startsWith("lintAnalyze")
    ) {
        dependsOn(syncWebGame)
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.webkit:webkit:1.12.0")
}
