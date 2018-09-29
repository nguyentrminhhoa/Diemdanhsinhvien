package com.example.hoa.quanlysv;

/**
 * Created by hoa on 9/7/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // ten dl
    private static final String DATABASE_NAME = "qlsv";


    private static final String TABLE_CONTACTS = "sinhvien";


    private static final String KEY_ID = "masv";
    private static final String KEY_NAME = "tensv";
    private static final String KEY_NS = "namsinh";
    private static final String KEY_TL = "tenlop";
    private static final String KEY_PH_NO = "gioitinh";
    private Context context;
    SQLiteDatabase database;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        Toast.makeText(context,"Tạo database",Toast.LENGTH_LONG).show();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Toast.makeText(context,"Tạo bảng",Toast.LENGTH_LONG).show();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_NS + " TEXT,"
                + KEY_TL + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }


    // expdata

    public Cursor getuser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_CONTACTS + " ",
                null);
        return res;
    }
    public void insertData(){
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("masv", "9999900");
        contentValues.put("tensv", "con gau");
        contentValues.put("namsinh", "2009");
        contentValues.put("tenlop", "dj");
        contentValues.put("gioitinh", "nu");
        db1.insert(TABLE_CONTACTS, null, contentValues);
    }
}