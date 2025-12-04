# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# ========== KOTLIN ==========
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ========== RETROFIT ==========
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# ========== GSON ==========
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**

-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Clases de datos (DTOs)
-keep class com.recordnote.data.remote.dto.** { *; }
-keep class com.recordnote.domain.model.** { *; }

# ========== ROOM ==========
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ========== HILT ==========
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
-keep class * extends androidx.lifecycle.ViewModel

-dontwarn com.google.errorprone.annotations.*

# ========== JETPACK COMPOSE ==========
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-dontwarn androidx.compose.**

# ========== WORKMANAGER ==========
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.CoroutineWorker
-keep class androidx.work.** { *; }

# ========== MODELO DE DOMINIO ==========
# Mantener todas las clases de modelo
-keep class com.recordnote.domain.** { *; }
-keep class com.recordnote.data.local.entidades.** { *; }

# ========== SERIALIZACIÓN ==========
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ========== ENUM ==========
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ========== PARCELABLE ==========
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ========== SERVICIOS DE ANDROID ==========
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# ========== NATIVE METHODS ==========
-keepclasseswithmembernames class * {
    native <methods>;
}

# ========== JAVASCRIPT INTERFACE ==========
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# ========== REFLECTION ==========
-keepattributes InnerClasses
-keep class **.R
-keep class **.R$* {
    <fields>;
}

# ========== OPTIMIZACIONES ==========
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# ========== WARNINGS ==========
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

# Google Cloud (si lo usas)
#-keep class com.google.cloud.** { *; }
#-dontwarn com.google.cloud.**

# OpenAI (si lo usas)
#-keep class com.aallam.openai.** { *; }
#-dontwarn com.aallam.openai.**