package com.teentitans.cakeproject.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.activities.MainActivity;
import com.teentitans.cakeproject.utils.CustomRecycleViewAdapter;
import com.teentitans.cakeproject.utils.RecipeVO;

import java.util.ArrayList;

public class RecipesFragment extends Fragment implements ObservableScrollViewCallbacks {

    public static final String ARG_TITLE = "title";
    public static final String ARG_RECIPES = "recipes";
    ArrayList<RecipeVO> recipes;
    private ObservableRecyclerView mRecyclerView;
    private CustomRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String title;

    public static RecipesFragment create(String title, ArrayList<RecipeVO> recipes) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putSerializable(ARG_RECIPES, recipes);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(ARG_TITLE);
        recipes = (ArrayList<RecipeVO>) getArguments().getSerializable(ARG_RECIPES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipes, container, false);

        mRecyclerView = (ObservableRecyclerView) v.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setScrollViewCallbacks(this);
        Activity parentActivity = getActivity();
        mRecyclerView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            mRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        if (title.equals("Search")) {
            //TODO search here (recipes = result)
            //recipes = null;
        }
        if (recipes != null) {
            mAdapter = new CustomRecycleViewAdapter(recipes, getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }

        return v;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }

}

