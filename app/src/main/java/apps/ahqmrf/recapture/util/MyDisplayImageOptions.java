package apps.ahqmrf.recapture.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import apps.ahqmrf.recapture.R;

/**
 * Created by Lenovo on 3/8/2017.
 */

public class MyDisplayImageOptions {
    private DisplayImageOptions displayImageOptions;
    private static MyDisplayImageOptions mInstance;

    private MyDisplayImageOptions() {
        displayImageOptions = getDisplayImageOptions();
    }

    public static synchronized MyDisplayImageOptions getInstance() {
        if(mInstance == null) {
            mInstance = new MyDisplayImageOptions();
        }
        return mInstance;
    }

    public DisplayImageOptions getDisplayImageOptions() {
        if(displayImageOptions == null) {
            displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.loading)
                .showImageOnFail(R.drawable.loading)
                .showImageOnLoading(R.drawable.loading)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();
        }
        return displayImageOptions;
    }
}
