# Desktop release: keep obfuscation, avoid Compose VerifyError.
# ProGuard's optimizer rewrites Compose/Kotlin bytecode incorrectly
# (java.lang.VerifyError: Bad return type). Shrinking + obfuscation are fine.

-dontoptimize
-ignorewarnings
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature,InnerClasses,EnclosingMethod
-renamesourcefileattribute SourceFile
-obfuscationdictionary ../androidApp/dictionary.txt
-packageobfuscationdictionary ../androidApp/dictionary.txt
-classobfuscationdictionary ../androidApp/dictionary.txt

# Entry point must remain discoverable by the desktop launcher.
-keep class github.leavesczy.compose_tetris.MainKt {
    public static void main(java.lang.String[]);
}

# Sound / AWT hooks used reflectively on desktop.
-keep class github.leavesczy.compose_tetris.DesktopSoundPlayer { *; }
-keep class github.leavesczy.compose_tetris.base.logic.SoundType { *; }
-dontwarn javax.sound.sampled.**