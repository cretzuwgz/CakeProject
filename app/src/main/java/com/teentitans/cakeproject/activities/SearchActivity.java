package com.teentitans.cakeproject.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.fragments.RecipesFragment;
import com.teentitans.cakeproject.utils.ConnectionUtil;
import com.teentitans.cakeproject.utils.RecipesUtil;

import java.io.IOException;

public class SearchActivity extends ActionBarActivity {

    public static String URL_TOP = "http://cakeproject.whostf.com/php/get_top5.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new GetRecipesTask().execute();
            //use the query to search your data somehow
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


    private class GetRecipesTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            try {
                response = ConnectionUtil.getResponseFromURL(URL_TOP);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, RecipesFragment.create("Search", RecipesUtil.getRecipesFrom(response))).commit();
            return "";
        }
    }
}
