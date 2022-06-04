package com.mpaz2001.android.structureinspectionapp.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mpaz2001.android.structureinspectionapp.database.DefectDbSchema.DefectTable;

public class DefectBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "defectBase.db";

    public DefectBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DefectTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                DefectTable.Cols.UUID + ", " +
                DefectTable.Cols.TITLE + ", " +
                DefectTable.Cols.DESCRIPTION + ", " +
                DefectTable.Cols.DATE + ", " +
                DefectTable.Cols.SOLVED + ", " +
                DefectTable.Cols.SUSPECT +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}