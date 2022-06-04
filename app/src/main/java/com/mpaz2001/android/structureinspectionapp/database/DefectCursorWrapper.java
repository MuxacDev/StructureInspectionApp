package com.mpaz2001.android.structureinspectionapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mpaz2001.android.structureinspectionapp.Defect;
import com.mpaz2001.android.structureinspectionapp.database.DefectDbSchema.DefectTable;

import java.util.Date;
import java.util.UUID;

import static com.mpaz2001.android.structureinspectionapp.database.DefectDbSchema.DefectTable.*;

public class DefectCursorWrapper extends CursorWrapper {

    public DefectCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Defect getDefect() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String title = getString(getColumnIndex(Cols.TITLE));
        String description = getString(getColumnIndex(Cols.DESCRIPTION));
        long date = getLong(getColumnIndex(Cols.DATE));
        int isSolved = getInt(getColumnIndex(Cols.SOLVED));
        String suspect = getString(getColumnIndex(DefectTable.Cols.SUSPECT));

        Defect defect = new Defect(UUID.fromString(uuidString));
        defect.setTitle(title);
        defect.setDescription(description);
        defect.setDate(new Date(date));
        defect.setSolved(isSolved != 0);
        defect.setSuspect(suspect);

        return defect;
    }
}
