package com.teentitans.cakeproject.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.activities.RegisterActivity;
import com.teentitans.cakeproject.utils.ConnectionUtil;
import com.teentitans.cakeproject.utils.UserVO;

import java.io.IOException;

public class RegisterPageFragment extends Fragment {

    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    private static UserVO user;
    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static RegisterPageFragment create(int pageNumber) {
        Log.e("Page", pageNumber + "");
        RegisterPageFragment fragment = new RegisterPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static UserVO getUser() {
        return user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mPageNumber == 1) {
            final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_register_step1, container, false);
            Button btnNext = (Button) rootView.findViewById(R.id.btnNext);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ButtonClicked", "ASD");
                    new CheckRegisterDataTask(rootView);
                }
            });
            return rootView;
        } else if (mPageNumber == 2) {
            final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_register_step2, container, false);
            return rootView;
        }
        return null;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    private class CheckRegisterDataTask extends AsyncTask<String, String, String> {

        final ViewGroup rootView;
        private ProgressDialog pDialog;

        public CheckRegisterDataTask(ViewGroup rootView) {
            this.rootView = rootView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Checking data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Login
         */
        protected String doInBackground(String... args) {

            EditText etUsername = (EditText) rootView.findViewById(R.id.etUsername);
            EditText etPassword = (EditText) rootView.findViewById(R.id.etPassword);

            if (etUsername.getText().length() < 4) {
                return "Username too short.";
            }
            Boolean userAlreadyExists = userAlreadyInDB(etUsername.getText().toString());

            if (userAlreadyExists == null) {
                return "Connection Failed";
            }

            if (userAlreadyExists) {
                return "Username already exists.";
            }

            if (etPassword.getText().length() < 4) {
                return "Password too short.";
            }

            Spinner genderSpinner = (Spinner) rootView.findViewById(R.id.sGender);
            Spinner expecienceSpinner = (Spinner) rootView.findViewById(R.id.sExperience);

            user = new UserVO(null, etUsername.getText().toString(), etPassword.getText().toString(), null, genderSpinner.getSelectedItem().toString(), expecienceSpinner.getSelectedItem().toString());

            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equals("OK"))
                ((RegisterActivity) getActivity()).nextStep();
            else {
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        }

        protected Boolean userAlreadyInDB(String username) {

            String url = "http://cakeproject.whostf.com/php/check_username.php";
            String parameters = "username=" + username;

            try {
                String response = ConnectionUtil.getResponseFromURL(url, parameters);
                if (response.equals(0))
                    return false;
                return true;
            } catch (IOException e) {
                return null;
            }
        }
    }

}
