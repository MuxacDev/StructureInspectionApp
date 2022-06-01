package com.mpaz2001.android.structureinspectionapp;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class DefectListActivity extends SingleFragmentActivity
        implements DefectListFragment.Callbacks, DefectFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new DefectListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onDefectSelected(Defect defect) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = DefectPagerActivity.newIntent(this, defect.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = DefectFragment.newInstance(defect.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    public void onDefectUpdated(Defect defect) {
        DefectListFragment listFragment = (DefectListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
