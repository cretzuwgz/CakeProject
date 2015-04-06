package com.teentitans.cakeproject.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.activities.MainActivity;
import com.teentitans.cakeproject.utils.CustomRecycleViewAdapter;

public class RecentFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_recent, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler);

        final Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
//        toolbar.post(new Runnable() {
//            @Override public void run() {
//                ScrollManager manager = new ScrollManager();
//                manager.attach(mRecyclerView);
//                manager.addView(toolbar, ScrollManager.Direction.UP);
//                manager.setInitialOffset(toolbar.getHeight());
//            }
//        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] dataset = {"ciocolata", "frisca", "alune,ciocolata"};
        mAdapter = new CustomRecycleViewAdapter(dataset);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

}
