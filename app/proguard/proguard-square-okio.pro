# from https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-square-okio.pro

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
