# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
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

# Support library
 -dontwarn android.support.**
 -dontwarn android.support.v4.**
 -keep class android.support.v4.** { *; }
 -keep interface android.support.v4.** { *; }
 -dontwarn android.support.v7.**
 -keep class android.support.v7.** { *; }
 -keep interface android.support.v7.** { *; }

# support design
 -dontwarn android.support.design.**
 -keep class android.support.design.** { *; }
 -keep interface android.support.design.** { *; }
 -keep public class android.support.design.R$* { *; }

# Needed for Parcelable/SafeParcelable Creators to not get stripped
 -keepnames class * implements android.os.Parcelable { public static final ** CREATOR;}

#default & basic optimization configurations
  -optimizationpasses 5
  -dontpreverify
  -repackageclasses ''
  -allowaccessmodification
  -optimizations !code/simplification/arithmetic
  -keepattributes *Annotation*

  -verbose

  -printseeds obfuscation/seeds.txt
  -printusage obfuscation/unused.txt



#Keep class names of Hilt injected ViewModels
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# The following rules are used to strip any non essential Google Play Services classes and method.
# For Google Play Services
  -keep public class com.google.android.gms.ads.**{ public *; }

# For old ads classes
 -keep public class com.google.ads.**{ public *; }

# For mediation
 -keepattributes *Annotation*

# Other required classes for Google Play Services
# Read more at http://developer.android.com/google/play-services/setup.html
 -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable { public static final *** NULL; }
 -keepnames @com.google.android.gms.common.annotation.KeepName class *
 -keepclassmembernames class * { @com.google.android.gms.common.annotation.KeepName *; }

 -keep class com.google.ads.**
 -dontwarn com.google.ads.**

 -keep class com.google.android.material.**{*;}
 -dontwarn com.google.android.material.**
 -dontnote com.google.android.material.**
# -keep class com.anorlddroid.patricemulindi.domain.**
# -keep class com.anorlddroid.patricemulindi.model.**
# -keep class com.anorlddroid.patricemulindi.usecases.**
# -keep class com.anorlddroid.patricemulindi.viewmodels.**
#  -keep class com.anorlddroid.patricemulindi.views.**

