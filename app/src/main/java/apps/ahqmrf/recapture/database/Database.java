package apps.ahqmrf.recapture.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import apps.ahqmrf.recapture.model.People;
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
        String query = "CREATE TABLE " + Constants.DBUtil.TablePeople.NAME + "(" +
                Constants.DBUtil.TablePeople.Column.ID + " INTEGER PRIMARY KEY, " +
                Constants.DBUtil.TablePeople.Column.NAME + " TEXT, " +
                Constants.DBUtil.TablePeople.Column.AVATAR_PATH + " TEXT, " +
                Constants.DBUtil.TablePeople.Column.RELATION + " TEXT " +
                ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DBUtil.TablePeople.NAME);
    }

    public void insertUser(People people) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(Constants.DBUtil.TablePeople.Column.NAME, people.getName());
        values.put(Constants.DBUtil.TablePeople.Column.AVATAR_PATH, people.getAvatar());
        values.put(Constants.DBUtil.TablePeople.Column.RELATION, people.getRelation());
        db.insert(Constants.DBUtil.TablePeople.NAME, null, values);
        db.close();
    }

    public ArrayList<People> getAllPeople() {
        ArrayList<People> peoples = new ArrayList<>();
        String query = "SELECT * FROM " + Constants.DBUtil.TablePeople.NAME + " WHERE 1;";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TablePeople.Column.NAME));
                String avatar = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TablePeople.Column.AVATAR_PATH));
                String relation = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TablePeople.Column.RELATION));
                People people = new People(name, avatar, relation);
                peoples.add(people);
            }
        } finally {
            cursor.close();
        }
        return peoples;
    }
}
