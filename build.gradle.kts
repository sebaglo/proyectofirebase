// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) version "8.1.0" apply false
    id ("com.android.library") version "8.1.0" apply false
    id ("com.google.gms.google-services") version "4.3.15" apply false
}