package apps.ahqmrf.recapture.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = mContext.getContentResolver().query(uri,  proj, null, null, null);
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

}
