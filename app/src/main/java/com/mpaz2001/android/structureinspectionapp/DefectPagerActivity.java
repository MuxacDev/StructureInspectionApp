package com.mpaz2001.android.structureinspectionapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class DefectPagerActivity extends AppCompatActivity
        implements DefectFragment.Callbacks {

    private static final String EXTRA_DEFECT_ID =
            "com.mpaz2001.android.structureinspectionapp.defect_id";

    private ViewPager mViewPager;
    private List<Defect> mDefects;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, DefectPagerActivity.class);
        intent.putExtra(EXTRA_DEFECT_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_pager);

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_DEFECT_ID);

        mViewPager = (ViewPager) findViewById(R.id.defect_view_pager);

        mDefects = DefectLab.get(this).getDefects();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Defect defect = mDefects.get(position);
                return DefectFragment.newInstance(defect.getId());
            }

            @Override
            public int getCount() {
                return mDefects.size();
            }
        });

        for (int i = 0; i < mDefects.size(); i++) {
            if (mDefects.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onDefectUpdated(Defect defect) {

    }
}
