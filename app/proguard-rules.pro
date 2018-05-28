# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Helps in debugging proguard configuration
# -printconfiguration config.txt

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontskipnonpubliclibraryclasses

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile, LineNumberTable, *Annotation*
-keepattributes Signature, InnerClasses

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keep class io.fabric.sdk.android.** { *; }
-dontwarn io.fabric.sdk.android.**

-keep public class * extends java.lang.Exception

# Play Services
# https://stackoverflow.com/a/24109609/944070
-keep class com.google.android.gms.** { *; }
-keep interface com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-dontnote com.google.android.gms.internal.measurement.**

-keep class com.google.firebase.FirebaseApp

# Evernote android-job
-dontwarn com.evernote.android.job.gcm.**
-dontwarn com.evernote.android.job.GcmAvailableHelper

-keep public class com.evernote.android.job.v21.PlatformJobService
-keep public class com.evernote.android.job.v14.PlatformAlarmService
-keep public class com.evernote.android.job.v14.PlatformAlarmReceiver
-keep public class com.evernote.android.job.JobBootReceiver
-keep public class com.evernote.android.job.JobRescheduleService
-keep public class com.evernote.android.job.gcm.PlatformGcmService

# My additional evernote config.
# Added for com.evernote.android.job.JobManager calls
#  '(com.evernote.android.job.JobCreator$AddJobCreatorReceiver)Class.forName(variable).newInstance()'
-keep class * implements com.evernote.android.job.JobCreator$AddJobCreatorReceiver
# I don't think that I need the GcmJob from evernote.
-dontnote com.evernote.android.job.GcmAvailableHelper


# Avoid dublicate warnining for org.apache.http and android.net.http
# Bug report https://issuetracker.google.com/issues/37070898
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**