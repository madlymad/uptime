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

-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-dontskipnonpubliclibraryclasses

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile, LineNumberTable, *Annotation*
-keepattributes Signature, InnerClasses

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep public class * extends java.lang.Exception

# Play Services
# https://stackoverflow.com/a/24109609/944070
-keep class com.google.android.gms.** { *; }
-keep class com.google.android.gms.internal.** { *; }
-keep interface com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-dontnote com.google.android.gms.**
-dontnote com.google.android.gms.internal.measurement.**

# Google Play Proguard
# https://stackoverflow.com/a/24109609/944070
-keep class * extends java.util.ListResourceBundle {
    protected java.lang.Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.google.firebase.**
-dontwarn com.google.firebase.**
-dontnote com.google.firebase.**

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

-dontnote com.google.android.material.**

-keepclassmembers enum androidx.lifecycle.Lifecycle$Event {
    <fields>;
}
-keep class * implements androidx.lifecycle.LifecycleObserver {
}
-keep class * implements androidx.lifecycle.GeneratedAdapter {
    <init>(...);
}
-keepclassmembers class ** {
    @androidx.lifecycle.OnLifecycleEvent *;
}

# Proguard config for project using GMS
-keepnames @com.google.android.gms.common.annotation.KeepName class  com.google.android.gms.**, com.google.ads.**
-keepclassmembernames class  com.google.android.gms.**, com.google.ads.** {
    @com.google.android.gms.common.annotation.KeepName *;
}
# Called by introspection
-keep class com.google.android.gms.**, com.google.ads.** extends java.util.ListResourceBundle {
    protected java.lang.Object[][] getContents();
}

# This keeps the class name as well as the creator field, because the
# "safe parcelable" can require them during unmarshalling.
-keepnames class com.google.android.gms.**, com.google.ads.** implements android.os.Parcelable {
    public static final ** CREATOR;
}
