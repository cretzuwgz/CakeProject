package com.teentitans.cakeproject.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.fragments.RecipesFragment;
import com.teentitans.cakeproject.utils.ConnectionUtil;
import com.teentitans.cakeproject.utils.RecipeVO;
import com.teentitans.cakeproject.utils.RecipesUtil;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends ActionBarActivity {

    public static String URL_TOP = "http://cakeproject.whostf.com/php/search.php";
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progress = new ProgressDialog(SearchActivity.this);

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
            new GetRecipesTask().execute(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.setQuery("", false);
                searchView.setIconified(true);
                Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
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

    private class GetRecipesTask extends AsyncTask<String, Void, RecipesFragment> {

        @Override
        protected void onPreExecute() {
            progress.setMessage("Searching...");
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected RecipesFragment doInBackground(String... params) {
            try {
                String response = ConnectionUtil.getResponseFromURL(URL_TOP, "searchString=" + params[0]);

                ArrayList<RecipeVO> recipes = RecipesUtil.getRecipesFrom(response);
                return RecipesFragment.create("Search", recipes);

            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(RecipesFragment fragment) {

            progress.dismiss();

            if (fragment != null) {
                if (fragment.getArguments().getSerializable("recipes") != null)
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
                else
                    Toast.makeText(SearchActivity.this, "Sorry, no recipes matched your search. Please try again", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(SearchActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();

        }
    }
}
