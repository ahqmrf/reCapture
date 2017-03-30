package apps.ahqmrf.recapture.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import apps.ahqmrf.recapture.util.Constants;

/**
 * Created by Lenovo on 3/29/2017.
 */

public class Database extends SQLiteOpenHelper {
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Constants.Basic.DATABASE_NAME, factory, Constants.Basic.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
