package apps.ahqmrf.recapture.util;

/**
 * Created by Lenovo on 3/28/2017.
 */

public final class Constants {
    public final class Basic {
        public static final int PROGRESS_BAR_DURATION = 500;
        public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
        public static final String DATABASE_NAME = "recapture.db";
        public static final int DATABASE_VERSION = 1;
        public static final String IMAGE_LIST_EXTRA = "__image__list";
        public static final String IMAGE_EXTRA = "Image__EXTRA";
        public static final String SHARED_PREF_NAME = "mySharedPref";
        public static final String USERNAME = "myUsername";
        public static final String PROFILE_IMAGE_PATH = "myProfilePhoto";
        public static final String FAVORITE_QUOTE = "myFavoriteQuote";
        public static final String ABOUT = "aboutMyself";
        public static final String LOCK_MODE = "lock_mode";
        public static final String PASSWORD =  "Pass_word" ;
        public static final String SLIDE_SHOW_TIME_INTERVAL = "slide_show_time_interval";
        public static final long SPLASH_SCREEN_DURATION = 2000;
    }

    public final class IntentExtras {
        public static final String IMAGE_PATH = "image__path";
        public static final String MEMORY_TITLE = "memory__title";
        public static final String MEMORY_DESCRIPTION = "memory__story";
        public static final String IMAGE_LIST_EXTRA = "__image__list";
        public static final String MEMORY = "__memory";
        public static final String PEOPLE = "__people";
        public static final String POSITION = "position";
        public static final String HAPPENED_DATE = "date_happened";
        public static final String HAPPENED_TIME = "time_happened";
    }

    public final class DBUtil {
        public final class TablePeople {
            public static final String NAME = "PeopleTable";
            public final class Column {
                public static final String ID = "user__id";
                public static final String HASH = "people___hash";
                public static final String NAME = "user__name";
                public static final String AVATAR_PATH = "avatar__path";
                public static final String RELATION = "relation";
                public static final String ABOUT = "__about__";
            }
        }

        public final class TableMemory {
            public static final String NAME = "MemoryTable";
            public final class Column {
                public static final String SPECIAL = "is_special";
                public static final String ID = "memory__id";
                public static final String TITLE = "title";
                public static final String DESCRIPTION = "description";
                public static final String ICON_PATH = "IconPath";
                public static final String DATE = "date";
                public static final String TIME_STAMP = "time__stamp";
                public static final String HAPPENED_DATE = "date_happened";
                public static final String HAPPENED_TIME = "time_happened";
            }
        }

        public final class TableImage {
            public static final String NAME = "TableImage";
            public final class Column {
                public static final String ID = "image__id";
                public static final String IMAGE_PATH = "imagePath";
                public static final String TIME_STAMP = "image_timeStamp";
            }
        }

        public final class TableTaggedPeople {
            public static final String NAME = "TaggedPeople";
            public final class Column {
                public static final String TIME_STAMP = "people_timeStamp";
                public static final String ID = "user__id";
                public static final String NAME = "user__name";
                public static final String HASH = "people___hash";
                public static final String AVATAR_PATH = "avatar__path";
                public static final String RELATION = "relation";
                public static final String ABOUT = "__about__";
            }
        }
    }

    public final class RequestCodes {
        public static final int GALLERY_BROWSE_REQ = 0;
        public static final int PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE = 1;
        public static final int GALLERY_BROWSE_REQ_CODE = 2;
        public static final int ADD_USER_REQ = 3;
    }
}