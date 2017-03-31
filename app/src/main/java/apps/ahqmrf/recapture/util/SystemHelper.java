package apps.ahqmrf.recapture.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import apps.ahqmrf.recapture.activity.CreateMemoryActivity;
import apps.ahqmrf.recapture.activity.GalleryActivity;
import apps.ahqmrf.recapture.model.Time;

/**
 * Created by Lenovo on 3/29/2017.
 */

public class SystemHelper {
    private Context mContext;

    public SystemHelper(Context context) {
        mContext = context;
    }


    public Time getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.Basic.DATE_FORMAT);
        Date date = new Date();
        String timeStr = dateFormat.format(date).toString();
        String tokens[] = timeStr.split(" ");
        return new Time(tokens[0], tokens[1]);
    }

    public String getRealPathFromUri(Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public String pathForImageLoader(Uri uri) {
        return "file://" + getRealPathFromUri(uri);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard((Activity) mContext);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

}
