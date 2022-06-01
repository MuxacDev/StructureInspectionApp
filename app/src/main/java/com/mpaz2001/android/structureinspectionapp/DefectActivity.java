package com.mpaz2001.android.structureinspectionapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class DefectActivity extends SingleFragmentActivity {

    private static final String EXTRA_DEFECT_ID =
            "com.mpaz2001.android.structureinspectionapp.crime_id";

    public static Intent newIntent(Context packageContext, UUID defectId) {
        Intent intent = new Intent(packageContext, DefectActivity.class);
        intent.putExtra(EXTRA_DEFECT_ID, defectId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID defectId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_DEFECT_ID);
        return DefectFragment.newInstance(defectId);
    }
}
