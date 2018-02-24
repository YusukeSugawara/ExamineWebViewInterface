package work.yusukesugawara.examinewebviewinterface.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

public class Str {
    @NonNull
    public static String format(@NonNull String format, @Nullable Object... args) {
        return String.format(Locale.US, format, args);
    }
}
