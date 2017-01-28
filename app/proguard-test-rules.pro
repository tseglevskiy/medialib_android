    -dontobfuscate
    -dontshrink

    # http://stackoverflow.com/a/35929242/1263771
    # Proguard rules that are applied to your test apk/code.
    -ignorewarnings

    -keepattributes *Annotation*

    -dontnote junit.framework.**
    -dontnote junit.runner.**

    -dontwarn android.test.**
    -dontwarn android.support.test.**
    -dontwarn org.junit.**
    -dontwarn org.hamcrest.**
    -dontwarn com.squareup.javawriter.JavaWriter

    -dontwarn org.mockito.**
