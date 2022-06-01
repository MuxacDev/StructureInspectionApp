package com.mpaz2001.android.structureinspectionapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DefectListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private DefectAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onDefectSelected(Defect defect);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_defect_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.defect_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_defect_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_defect:
                Defect defect = new Defect();
                DefectLab.get(getActivity()).addDefect(defect);
                updateUI();
                mCallbacks.onDefectSelected(defect);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        DefectLab defectLab = DefectLab.get(getActivity());
        int crimeCount = defectLab.getDefects().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateUI() {
        DefectLab defectLab = DefectLab.get(getActivity());
        List<Defect> defects = defectLab.getDefects();

        if (mAdapter == null) {
            mAdapter = new DefectAdapter(defects);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(defects);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class DefectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Defect mDefect;

        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private final ImageView mSolvedImageView;

        public DefectHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_defect, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.defect_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.defect_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.defect_solved);
        }

        public void bind(Defect defect) {
            mDefect = defect;
            mTitleTextView.setText(mDefect.getTitle());
            mDateTextView.setText(mDefect.getDate().toString());
            mSolvedImageView.setVisibility(defect.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onDefectSelected(mDefect);
        }
    }

    private class DefectAdapter extends RecyclerView.Adapter<DefectHolder> {

        private List<Defect> mDefects;

        public DefectAdapter(List<Defect> defects) {
            mDefects = defects;
        }

        @Override
        public DefectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new DefectHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(DefectHolder holder, int position) {
            Defect defect = mDefects.get(position);
            holder.bind(defect);
        }

        @Override
        public int getItemCount() {
            return mDefects.size();
        }

        public void setCrimes(List<Defect> defects) {
            mDefects = defects;
        }
    }
}
