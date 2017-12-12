package com.teentitans.cakeproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.utils.CustomRecycleViewAdapter;
import com.teentitans.cakeproject.utils.RecipeVO;

import java.util.ArrayList;

public class RecipesFragment extends Fragment {

    public static final String ARG_TITLE = "_title";
    public static final String ARG_RECIPES = "_recipes";

    private ArrayList<RecipeVO> _recipes;
    private String _title = "";

    public static RecipesFragment create(String title, ArrayList<RecipeVO> recipes) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putSerializable(ARG_RECIPES, recipes);
        fragment.setArguments(args);
        return fragment;
    }

    public String getTitle() {

        return _title;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        _title = getArguments().getString(ARG_TITLE);
        _recipes = (ArrayList<RecipeVO>) getArguments().getSerializable(ARG_RECIPES);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_recipes, container, false);

        ObservableRecyclerView mRecyclerView = v.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);

        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        mRecyclerView.setTouchInterceptionViewGroup(parentActivity.findViewById(R.id.container));

        if (parentActivity instanceof ObservableScrollViewCallbacks)
            mRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (_recipes != null && !_recipes.isEmpty()) {
            CustomRecycleViewAdapter mAdapter = new CustomRecycleViewAdapter(_recipes, parentActivity);
            mRecyclerView.setAdapter(mAdapter);
        }

        return v;
    }
}

