package apps.ahqmrf.recapture.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

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
                    .build();
        }
        return displayImageOptions;
    }
}
