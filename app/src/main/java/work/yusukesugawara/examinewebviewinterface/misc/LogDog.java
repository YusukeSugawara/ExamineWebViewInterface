package work.yusukesugawara.examinewebviewinterface.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class LogDog {
    public static void d(@NonNull String tag, @NonNull String text) {
        Log.d(tag, text);
    }

    public static void d(@NonNull String tag, @NonNull String format, @Nullable Object... args) {
        Log.d(tag, Str.format(format, args));
    }
}
