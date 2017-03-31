package apps.ahqmrf.recapture.util;

/**
 * Created by Lenovo on 3/28/2017.
 */

public final class Constants {
    public final class Basic {
        public static final int SPLASH_SCREEN_DURATION = 4000;
        public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
        public static final String DATABASE_NAME = "recapture.db";
        public static final int DATABASE_VERSION = 1;
        public static final String IMAGE_LIST_EXTRA = "__image__list";
    }

    public final class IntentExtras {
        public static final String IMAGE_PATH = "image__path";
        public static final String MEMORY_TITLE = "memory__title";
        public static final String MEMORY_DESCRIPTION = "memory__story";
        public static final String IMAGE_LIST_EXTRA = "__image__list";
        public static final String MEMORY = "__memory";
    }

    public final class DBUtil {

    }

    public final class RequestCodes {
        public static final int GALLERY_BROWSE_REQ = 0;
    }
}