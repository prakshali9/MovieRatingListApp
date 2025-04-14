plugins {
    id("com.android.application") version "8.8.2" apply false
    id("com.android.library") version "8.8.2" apply false
    kotlin("android") version "1.9.10" apply false
    kotlin("kapt") version "1.9.10" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}