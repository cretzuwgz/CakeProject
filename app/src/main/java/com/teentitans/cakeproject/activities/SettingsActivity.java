package com.teentitans.cakeproject.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.utils.ConnectionUtil;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private static SettingsActivity ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ACTIVITY = this;
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etOldPwd = findViewById(R.id.etOldPwd);
        EditText etNewPwd = findViewById(R.id.etNewPwd);
        EditText etTags = findViewById(R.id.etTags);

        Button btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
        Button btnChangePwd = findViewById(R.id.btnChangePwd);
        Button btnUpdateTags = findViewById(R.id.btnUpdateTags);

        Spinner sGender = findViewById(R.id.sGender);
        Spinner sExperience = findViewById(R.id.sExperience);

        btnUpdateInfo.setOnClickListener(view -> {
            if (!etUsername.getText().toString().equals(MainActivity.getUser().getUsername())
                    || sGender.getSelectedItemPosition() + 1 != MainActivity.getUser().getGender()
                    || sExperience.getSelectedItemPosition() + 1 != MainActivity.getUser().getExperience())
                new UpdateInfoTask().execute(MainActivity.getUser().getId(), etUsername.getText().toString(), String.valueOf(sGender.getSelectedItemPosition() + 1), String.valueOf(sExperience.getSelectedItemPosition() + 1));
        });

        btnChangePwd.setOnClickListener(view -> {
            if (!etOldPwd.getText().toString().isEmpty() && !etNewPwd.getText().toString().isEmpty()) {
                new ChangePasswordTask().execute(MainActivity.getUser().getId(), etOldPwd.getText().toString(), etNewPwd.getText().toString());
            } else
                Toast.makeText(ACTIVITY, "Both fields are required", Toast.LENGTH_LONG).show();
        });

        btnUpdateTags.setOnClickListener(v -> {
            if (!etTags.getText().toString().isEmpty() && !etTags.getText().toString().equals(MainActivity.getUser().getFavoriteTagsAsString()))
                new UpdateFavoriteTagsTask().execute(MainActivity.getUser().getId(), etTags.getText().toString());
            else
                Toast.makeText(ACTIVITY, "Please check tags!", Toast.LENGTH_LONG).show();
        });

        etUsername.setText(MainActivity.getUser().getUsername());

        etTags.setText(MainActivity.getUser().getFavoriteTagsAsString());

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

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.setQuery("", false);
                searchView.setIconified(true);
                Intent intent = new Intent(ACTIVITY, SearchActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, s);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private static class UpdateInfoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String parameters = "userId=" + params[0] + "&username=" + params[1] + "&gender=" + params[2] + "&experience=" + params[3];

            String response = null;
            try {
                response = ConnectionUtil.getResponseFromURL(ConnectionUtil.URL_BASE + "update_user.php", parameters);
            } catch (IOException e) {
                Log.e("Update user", "error");
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null)
                Toast.makeText(ACTIVITY, R.string.error_message, Toast.LENGTH_LONG).show();
            else if (!response.equals("OK"))
                Toast.makeText(ACTIVITY, "Info changed", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ACTIVITY, response, Toast.LENGTH_LONG).show();
        }
    }

    private static class ChangePasswordTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String parameters = "userId=" + params[0] + "&oldPass=" + params[1] + "&newPass=" + params[2];
            String response = null;
            try {
                response = ConnectionUtil.getResponseFromURL(ConnectionUtil.URL_BASE + "change_pass.php", parameters);
            } catch (IOException e) {
                Log.e("Update user", "error");
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null)
                Toast.makeText(ACTIVITY, R.string.error_message, Toast.LENGTH_LONG).show();
            else if (response.equals("FAIL"))
                Toast.makeText(ACTIVITY, "Old password is wrong", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ACTIVITY, "Password changed", Toast.LENGTH_LONG).show();
        }
    }

    private static class UpdateFavoriteTagsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String response = null;

            try {
                response = ConnectionUtil.getResponseFromURL("https://cakeproject.000webhostapp.com/change_tags.php", "user_id" + params[0] + "&tags=" + params[1]);
            } catch (IOException e) {
                Log.e("Update favorite tags", "error");
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {

            if (response == null)
                Toast.makeText(ACTIVITY, R.string.error_message, Toast.LENGTH_LONG).show();
            else {
                MainActivity.setToUpdate();
                Toast.makeText(ACTIVITY, response, Toast.LENGTH_LONG).show();
            }
        }
    }
}