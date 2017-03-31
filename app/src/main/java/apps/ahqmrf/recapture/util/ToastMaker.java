package apps.ahqmrf.recapture.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Lenovo on 3/13/2017.
 */

public final class ToastMaker {
    public static void showShortMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}