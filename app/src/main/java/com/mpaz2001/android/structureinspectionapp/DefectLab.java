package com.mpaz2001.android.structureinspectionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mpaz2001.android.structureinspectionapp.database.DefectBaseHelper;
import com.mpaz2001.android.structureinspectionapp.database.DefectCursorWrapper;
import com.mpaz2001.android.structureinspectionapp.database.DefectDbSchema.DefectTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mpaz2001.android.structureinspectionapp.database.DefectDbSchema.DefectTable.Cols.*;

public class DefectLab {
    private static DefectLab sDefectLab;
    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    public static DefectLab get(Context context) {
        if (sDefectLab == null) {
            sDefectLab = new DefectLab(context);
        }

        return sDefectLab;
    }

    private DefectLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DefectBaseHelper(mContext)
                .getWritableDatabase();

    }

    public void addDefect(Defect c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(DefectTable.NAME, null, values);
    }

    public List<Defect> getDefects() {
        List<Defect> defects = new ArrayList<>();
        DefectCursorWrapper cursor = queryDefects(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                defects.add(cursor.getDefect());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return defects;
    }

    public Defect getDefect(UUID id) {
        DefectCursorWrapper cursor = queryDefects(
                DefectTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getDefect();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Defect defect) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, defect.getPhotoFilename());
    }

    public void updateDefect(Defect defect) {
        String uuidString = defect.getId().toString();
        ContentValues values = getContentValues(defect);
        mDatabase.update(DefectTable.NAME, values,
                DefectTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private DefectCursorWrapper queryDefects(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DefectTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new DefectCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Defect defect) {
        ContentValues values = new ContentValues();
        values.put(UUID, defect.getId().toString());
        values.put(TITLE, defect.getTitle());
        values.put(DESCRIPTION, defect.getDescription());
        values.put(DATE, defect.getDate().getTime());
        values.put(SOLVED, defect.isSolved() ? 1 : 0);
        values.put(DefectTable.Cols.SUSPECT, defect.getSuspect());

        return values;
    }
}
