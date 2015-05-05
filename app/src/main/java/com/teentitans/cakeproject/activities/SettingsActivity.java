package com.teentitans.cakeproject.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.utils.ConnectionUtil;

import java.io.IOException;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etOldPwd = (EditText) findViewById(R.id.etOldPwd);
        final EditText etNewPwd = (EditText) findViewById(R.id.etNewPwd);
        EditText etTags = (EditText) findViewById(R.id.etTags);

        Button btnUpdateInfo = (Button) findViewById(R.id.btnUpdateInfo);
        Button btnChangePwd = (Button) findViewById(R.id.btnChangePwd);
        Button btnUpdateTags = (Button) findViewById(R.id.btnUpdateTags);

        final Spinner sGender = (Spinner) findViewById(R.id.sGender);
        final Spinner sExperience = (Spinner) findViewById(R.id.sExperience);

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etUsername.getText().toString().equals(MainActivity.getUser().getUsername())
                        || sGender.getSelectedItemPosition() + 1 != MainActivity.getUser().getGender()
                        || sExperience.getSelectedItemPosition() + 1 != MainActivity.getUser().getExperience())
                    new UpdateInfoTask().execute(MainActivity.getUser().getId(), etUsername.getText().toString(), String.valueOf(sGender.getSelectedItemPosition() + 1), String.valueOf(sExperience.getSelectedItemPosition() + 1));
            }
        });

        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etOldPwd.getText().toString().isEmpty() && !etNewPwd.getText().toString().isEmpty()) {
                    new ChangePasswordTask().execute(MainActivity.getUser().getId(), etOldPwd.getText().toString(), etNewPwd.getText().toString());
                } else
                    Toast.makeText(SettingsActivity.this, "Both fields are required", Toast.LENGTH_LONG).show();
            }
        });

        etUsername.setText(MainActivity.getUser().getUsername());

        for (String tag : MainActivity.getUser().getFavoriteTags()) {
            etTags.append(tag);
            etTags.append(",");
        }
        String text = etTags.getText().toString();
        Log.e("AICI", text);
        etTags.setText(text.substring(0, text.length() - 1));

        sGender.setSelection(MainActivity.getUser().getGender() - 1);
        sExperience.setSelection(MainActivity.getUser().getExperience() - 1);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public class UpdateInfoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String parameters = "userId=" + params[0] + "&username=" + params[1] + "&gender=" + params[2] + "&experience=" + params[3];

            String response = null;
            try {
                response = ConnectionUtil.getResponseFromURL("http://cakeproject.whostf.com/php/update_user.php", parameters);
            } catch (IOException e) {
                Log.e("Update user", "error");
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (!response.equals("OK"))
                Toast.makeText(SettingsActivity.this, response, Toast.LENGTH_LONG).show();
        }
    }

    public class ChangePasswordTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String parameters = "userId=" + params[0] + "&oldPass=" + params[1] + "&newPass=" + params[2];
            String response = null;
            try {
                response = ConnectionUtil.getResponseFromURL("http://cakeproject.whostf.com/php/change_pass.php", parameters);
            } catch (IOException e) {
                Log.e("Update user", "error");
            }

            return response;
        }

        //TODO check this with php
        @Override
        protected void onPostExecute(String response) {
            if (response == null)
                Toast.makeText(SettingsActivity.this, R.string.error_message, Toast.LENGTH_LONG).show();
            else if (response.equals("FAIL"))
                Toast.makeText(SettingsActivity.this, "Old password is wrong", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(SettingsActivity.this, "Password changed", Toast.LENGTH_LONG).show();
        }
    }
}