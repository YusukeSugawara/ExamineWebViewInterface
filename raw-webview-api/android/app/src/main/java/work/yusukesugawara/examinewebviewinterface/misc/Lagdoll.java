package work.yusukesugawara.examinewebviewinterface.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class Lagdoll {
    public static void d(@NonNull String tag, @NonNull String text) {
        Log.d(tag, text);
    }

    public static void d(@NonNull String tag, @NonNull String format, @Nullable Object... args) {
        Lagdoll.d(tag, Str.format(format, args));
    }

    public static void w(@NonNull String tag, @NonNull String text) {
        Log.w(tag, text);
    }

    public static void w(@NonNull String tag, @NonNull String format, @Nullable Object... args) {
        Lagdoll.w(tag, Str.format(format, args));
    }
}
