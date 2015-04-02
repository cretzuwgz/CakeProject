package com.teentitans.cakeproject.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.activities.RegisterActivity;

public class RegisterPageFragment extends Fragment {

    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static RegisterPageFragment create(int pageNumber) {
        RegisterPageFragment fragment = new RegisterPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = null;

        if (mPageNumber == 1) {
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_register_step1, container, false);
            Button next = (Button) rootView.findViewById(R.id.btnNext);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((RegisterActivity) getActivity()).nextStep();
                }
            });
        } else if (mPageNumber == 2)
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_register_step2, container, false);

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

}
