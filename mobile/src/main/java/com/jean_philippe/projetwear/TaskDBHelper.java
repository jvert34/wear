package com.jean_philippe.projetwear;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ferdousur Rahman Sarker on 3/19/2018.
 */

public class TaskDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDoDBHelper.db";
    public static final String CONTACTS_TABLE_NAME = "todo";

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(
                "CREATE TABLE " + CONTACTS_TABLE_NAME +
                        "(id INTEGER PRIMARY KEY, category TEXT, task TEXT, dateStr INTEGER, description TEXT, hour TEXT)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }


    private long getDate(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(day);
        } catch (ParseException e) {
        }
        assert date != null;
        return date.getTime();
    }


    public boolean insertContact(String category, String task, String description) {
        Date date;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", category);
        contentValues.put("task", task);
        contentValues.put("description", description);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateContact(String id, String category, String task, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("category", category);
        contentValues.put("task", task);
        contentValues.put("description", description);

        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[]{id});
        return true;
    }


    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " order by id desc", null);
        return res;

    }

    public Cursor getDataSpecific(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " WHERE id = '" + id + "' order by id desc", null);
        return res;

    }

    public Cursor getDataToday() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by id desc", null);
        return res;

    }


    public Cursor getDataTomorrow() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by id desc", null);
        return res;

    }


    public Cursor getDataUpcoming() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by id desc", null);
        return res;

    }

    public List<ModeleDonnees> getdata() {
        // DataModel dataModel = new DataModel();
        List<ModeleDonnees> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " ;", null);
        StringBuffer stringBuffer = new StringBuffer();
        ModeleDonnees ModeleD = null;
        while (cursor.moveToNext()) {
            ModeleD = new ModeleDonnees();
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("task"));
            String gps_lat = cursor.getString(cursor.getColumnIndexOrThrow("lat"));
            String gps_long = cursor.getString(cursor.getColumnIndexOrThrow("long"));
            String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
//            String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));
//            String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
            ModeleD.setCategory(category);
            ModeleD.setStudent_id(name);
            ModeleD.setGps_lat(gps_lat);
            ModeleD.setGps_long(gps_long);
            ModeleD.setMessage(message);

            stringBuffer.append(ModeleD);
            data.add(ModeleD);
        }

        for (ModeleDonnees modeleDonnees : data) {

            Log.i("HelloMD", "" + modeleDonnees.getStudent_id());
        }

        //

        return data;
    }
}
