package com.khasang.fixmynumber.Helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final String RESERVE_DB = "reserve.db";
    public static final int DB_VERSION = 1;
    public static final String MY_TABLE = "MY_TABLE";
    public static final String NAME = "NAME";
    public static final String PHONE = "PHONE";
    public static final String PHONE_ID = "PHONE_ID";
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String SQLITE_SEQUENCE = "sqlite_sequence";

    public DBHelper(Context context) {
        super(context, RESERVE_DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public static boolean dropTable(SQLiteDatabase db, String table) {
        return DBHelper.execSQL(db, "DROP TABLE IF EXISTS " + table);
    }

    private static boolean execSQL(SQLiteDatabase db, String sql) {
        if (db == null) return false;
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
