package apps.ahqmrf.recapture.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;

import apps.ahqmrf.recapture.model.ImageWithCaption;
import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.model.People;
import apps.ahqmrf.recapture.model.PeopleToTag;
import apps.ahqmrf.recapture.model.PeopleUtil;
import apps.ahqmrf.recapture.model.Time;
import apps.ahqmrf.recapture.util.Constants;
import apps.ahqmrf.recapture.util.MemoryComparator;
import apps.ahqmrf.recapture.util.PeopleComparator;

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
                Constants.DBUtil.TablePeople.Column.HASH + " TEXT, " +
                Constants.DBUtil.TablePeople.Column.AVATAR_PATH + " TEXT, " +
                Constants.DBUtil.TablePeople.Column.ABOUT + " TEXT, " +
                Constants.DBUtil.TablePeople.Column.RELATION + " TEXT " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + Constants.DBUtil.TableImage.NAME + "(" +
                Constants.DBUtil.TableImage.Column.ID + " INTEGER PRIMARY KEY, " +
                Constants.DBUtil.TableImage.Column.TIME_STAMP + " TEXT, " +
                Constants.DBUtil.TableImage.Column.IMAGE_PATH + " TEXT " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + Constants.DBUtil.TableTaggedPeople.NAME + "(" +
                Constants.DBUtil.TableTaggedPeople.Column.ID + " INTEGER PRIMARY KEY, " +
                Constants.DBUtil.TableTaggedPeople.Column.TIME_STAMP + " TEXT, " +
                Constants.DBUtil.TableTaggedPeople.Column.NAME + " TEXT, " +
                Constants.DBUtil.TableTaggedPeople.Column.HASH + " TEXT, " +
                Constants.DBUtil.TableTaggedPeople.Column.AVATAR_PATH + " TEXT, " +
                Constants.DBUtil.TableTaggedPeople.Column.ABOUT + " TEXT, " +
                Constants.DBUtil.TableTaggedPeople.Column.RELATION + " TEXT " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + Constants.DBUtil.TableMemory.NAME + "(" +
                Constants.DBUtil.TableMemory.Column.ID + " INTEGER PRIMARY KEY, " +
                Constants.DBUtil.TableMemory.Column.TIME_STAMP + " TEXT, " +
                Constants.DBUtil.TableMemory.Column.SPECIAL + " TEXT, " +
                Constants.DBUtil.TableMemory.Column.DATE + " TEXT, " +
                Constants.DBUtil.TableMemory.Column.TITLE + " TEXT, " +
                Constants.DBUtil.TableMemory.Column.ICON_PATH + " TEXT, " +
                Constants.DBUtil.TableMemory.Column.HAPPENED_DATE + " TEXT, " +
                Constants.DBUtil.TableMemory.Column.HAPPENED_TIME + " TEXT, " +
                Constants.DBUtil.TableMemory.Column.DESCRIPTION + " TEXT " +
                ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DBUtil.TablePeople.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DBUtil.TableImage.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DBUtil.TableTaggedPeople.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DBUtil.TableMemory.NAME);
    }

    public void insertUser(People people) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(Constants.DBUtil.TablePeople.Column.NAME, people.getName());
        values.put(Constants.DBUtil.TablePeople.Column.AVATAR_PATH, people.getAvatar());
        values.put(Constants.DBUtil.TablePeople.Column.RELATION, people.getRelation());
        values.put(Constants.DBUtil.TablePeople.Column.HASH, people.getHashValue());
        values.put(Constants.DBUtil.TablePeople.Column.ABOUT, people.getAbout());
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
                String hashValue = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TablePeople.Column.HASH));
                String about = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TablePeople.Column.ABOUT));
                People people = new People(name, avatar, relation, hashValue, about);
                peoples.add(people);
            }
        } finally {
            cursor.close();
        }
        Collections.sort(peoples, new PeopleComparator());
        return peoples;
    }

    public void insertImage(String timeStamp, String path) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(Constants.DBUtil.TableImage.Column.TIME_STAMP, timeStamp);
        values.put(Constants.DBUtil.TableImage.Column.IMAGE_PATH, path);
        db.insert(Constants.DBUtil.TableImage.NAME, null, values);
        db.close();
    }

    public void insertAllImages(Memory memory) {
        String timeStamp = memory.getTime().getTimeStamp();
        ArrayList<String> images = memory.getImages();
        for (String path : images) {
            insertImage(timeStamp, path);
        }
    }

    public ArrayList<String> getAllImages(Memory memory) {
        return getAllImages(memory.getTime().getTimeStamp());
    }

    private ArrayList<String> getAllImages(String timeStamp) {
        ArrayList<String> images = new ArrayList<>();
        String query = "SELECT * FROM " + Constants.DBUtil.TableImage.NAME + " WHERE " + Constants.DBUtil.TableImage.Column.TIME_STAMP + " = '" + timeStamp + "';";
        ;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableImage.Column.IMAGE_PATH));
                images.add(path);
            }
        } finally {
            cursor.close();
        }
        return images;
    }

    public void insertTaggedPeople(String timeStamp, People people) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(Constants.DBUtil.TableTaggedPeople.Column.TIME_STAMP, timeStamp);
        values.put(Constants.DBUtil.TableTaggedPeople.Column.AVATAR_PATH, people.getAvatar());
        values.put(Constants.DBUtil.TableTaggedPeople.Column.NAME, people.getName());
        values.put(Constants.DBUtil.TableTaggedPeople.Column.RELATION, people.getRelation());
        values.put(Constants.DBUtil.TableTaggedPeople.Column.HASH, people.getHashValue());
        values.put(Constants.DBUtil.TableTaggedPeople.Column.ABOUT, people.getAbout());
        db.insert(Constants.DBUtil.TableTaggedPeople.NAME, null, values);
        db.close();
    }

    public void insertAllTaggedPeople(Memory memory) {
        String timeStamp = memory.getTime().getTimeStamp();
        ArrayList<People> peoples = memory.getPeoples();
        for (People people : peoples) {
            insertTaggedPeople(timeStamp, people);
        }
    }

    public void insertAllTaggedPeople(String timeStamp, ArrayList<People> peoples) {
        for (People people : peoples) {
            insertTaggedPeople(timeStamp, people);
        }
    }

    public ArrayList<People> getAllTaggedPeople(Memory memory) {
        return getAllTaggedPeople(memory.getTime().getTimeStamp());
    }

    public ArrayList<People> getAllTaggedPeople(String timeStamp) {
        ArrayList<People> peoples = new ArrayList<>();
        String query = "SELECT * FROM " + Constants.DBUtil.TableTaggedPeople.NAME + " WHERE " + Constants.DBUtil.TableTaggedPeople.Column.TIME_STAMP + " = '" + timeStamp + "';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.NAME));
                String avatar = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.AVATAR_PATH));
                String relation = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.RELATION));
                String hashValue = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.HASH));
                String about = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.ABOUT));
                People people = new People(name, avatar, relation, hashValue, about);
                peoples.add(people);
            }
        } finally {
            cursor.close();
        }
        Collections.sort(peoples, new PeopleComparator());
        return peoples;
    }

    public ArrayList<PeopleUtil> getAllTaggedPeopleByHash(String hash) {
        ArrayList<PeopleUtil> peoples = new ArrayList<>();
        String query = "SELECT * FROM " + Constants.DBUtil.TableTaggedPeople.NAME + " WHERE " + Constants.DBUtil.TableTaggedPeople.Column.HASH + " = '" + hash + "';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.NAME));
                String avatar = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.AVATAR_PATH));
                String relation = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.RELATION));
                String hashValue = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.HASH));
                String about = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.ABOUT));
                String timeStamp = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableTaggedPeople.Column.TIME_STAMP));
                People people = new People(name, avatar, relation, hashValue, about);
                peoples.add(new PeopleUtil(people, timeStamp));
            }
        } finally {
            cursor.close();
        }

        return peoples;
    }


    public void insertMemory(Memory memory) {
        insertAllImages(memory);
        insertAllTaggedPeople(memory);
        insertMemoryData(memory);
    }

    public void insertMemoryData(Memory memory) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(Constants.DBUtil.TableMemory.Column.TIME_STAMP, memory.getTime().getTimeStamp());
        values.put(Constants.DBUtil.TableMemory.Column.DATE, memory.getTime().getDate());
        values.put(Constants.DBUtil.TableMemory.Column.TITLE, memory.getTitle());
        values.put(Constants.DBUtil.TableMemory.Column.HAPPENED_DATE, memory.getHappenedDate());
        values.put(Constants.DBUtil.TableMemory.Column.HAPPENED_TIME, memory.getHappenedTime());
        values.put(Constants.DBUtil.TableMemory.Column.DESCRIPTION, memory.getDescription());
        values.put(Constants.DBUtil.TableMemory.Column.ICON_PATH, memory.getIconPath());
        values.put(Constants.DBUtil.TableMemory.Column.SPECIAL, memory.isSpecial() ? "1" : "0");
        db.insert(Constants.DBUtil.TableMemory.NAME, null, values);
        db.close();
    }

    public ArrayList<Memory> getAllMemories() {
        ArrayList<Memory> memories = new ArrayList<>();
        String query = "SELECT * FROM " + Constants.DBUtil.TableMemory.NAME + " WHERE 1;";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableMemory.Column.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableMemory.Column.DESCRIPTION));
                String iconPath = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableMemory.Column.ICON_PATH));
                String timeStamp = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableMemory.Column.TIME_STAMP));
                String date = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableMemory.Column.DATE));
                ArrayList<People> peoples = getAllTaggedPeople(timeStamp);
                ArrayList<String> images = getAllImages(timeStamp);
                String hDate = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableMemory.Column.HAPPENED_DATE));
                String hTime = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableMemory.Column.HAPPENED_TIME));
                String special = cursor.getString(cursor.getColumnIndex(Constants.DBUtil.TableMemory.Column.SPECIAL));
                Memory memory = new Memory(
                        title,
                        description,
                        iconPath,
                        images,
                        peoples,
                        new Time(timeStamp, date),
                        hDate,
                        hTime,
                        special.equals("1")

                );
                memories.add(memory);
            }
        } finally {
            cursor.close();
        }
        Collections.sort(memories, new MemoryComparator());
        return memories;
    }

    public void remove(Memory memory) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.DBUtil.TableMemory.NAME + " WHERE " + Constants.DBUtil.TableMemory.Column.TIME_STAMP + " = '" + memory.getTime().getTimeStamp() + "';");
        db.close();
        removeImages(memory);
        removeTaggedPeople(memory);
    }

    public void removeImages(Memory memory) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.DBUtil.TableImage.NAME + " WHERE " + Constants.DBUtil.TableImage.Column.TIME_STAMP + " = '" + memory.getTime().getTimeStamp() + "';");
        db.close();
    }

    public void removeTaggedPeople(Memory memory) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.DBUtil.TableTaggedPeople.NAME + " WHERE " + Constants.DBUtil.TableTaggedPeople.Column.TIME_STAMP + " = '" + memory.getTime().getTimeStamp() + "';");
        db.close();
    }

    public void removeTaggedPeople(String timeStamp) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.DBUtil.TableTaggedPeople.NAME + " WHERE " + Constants.DBUtil.TableTaggedPeople.Column.TIME_STAMP + " = '" + timeStamp + "';");
        db.close();
    }

    public void remove(People people) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.DBUtil.TablePeople.NAME + " WHERE " + Constants.DBUtil.TablePeople.Column.HASH + " = '" + people.getHashValue() + "';");
        db.execSQL("DELETE FROM " + Constants.DBUtil.TableTaggedPeople.NAME + " WHERE " + Constants.DBUtil.TableTaggedPeople.Column.HASH + " = '" + people.getHashValue() + "';");
        db.close();
    }

    public void updateMemory(Memory memory) {
        remove(memory);
        insertMemory(memory);
    }

    public void updatePeople(People people) {
        ArrayList<PeopleUtil> tagged = getAllTaggedPeopleByHash(people.getHashValue());
        remove(people);
        insertUser(people);
        for(PeopleUtil util : tagged) {
            insertTaggedPeople(util.getTimeStamp(), people);
        }
    }

}
