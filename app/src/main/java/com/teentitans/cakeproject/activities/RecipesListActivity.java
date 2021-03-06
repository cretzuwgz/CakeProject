package com.teentitans.cakeproject.activities;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.fragments.RecipesFragment;
import com.teentitans.cakeproject.utils.ConnectionUtil;
import com.teentitans.cakeproject.utils.RecipeVO;
import com.teentitans.cakeproject.utils.RecipesUtil;

import java.io.IOException;
import java.util.ArrayList;

public class RecipesListActivity extends AppCompatActivity {

    private static RecipesListActivity ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY = this;
        setContentView(R.layout.activity_recipes_list);

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String activityFor = getIntent().getStringExtra("activityFor");
        new GetRecipesTask().execute(activityFor);
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
                Intent intent = new Intent(RecipesListActivity.this, SearchActivity.class);
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

    private static class GetRecipesTask extends AsyncTask<String, Void, RecipesFragment> {

        private ProgressDialog _progressDialog;

        @Override
        protected void onPreExecute() {

            _progressDialog = new ProgressDialog(ACTIVITY);
            _progressDialog.setMessage("Loading...");
            _progressDialog.setIndeterminate(true);
            _progressDialog.show();
        }

        @Override
        protected RecipesFragment doInBackground(String... params) {
            try {
                String response;
                ArrayList<RecipeVO> recipes;
                if (params[0].equals("uploaded")) {
                    response = ConnectionUtil.getResponseFromURL(ConnectionUtil.URL_BASE + "get_uploaded_recipes.php", "user_id=" + MainActivity.getUser().getId());
                    recipes = RecipesUtil.getRecipesFrom(response);
                    Log.w("uploaded", recipes.toString());
                    return RecipesFragment.create("Uploaded", recipes);
                } else {
                    response = ConnectionUtil.getResponseFromURL(ConnectionUtil.URL_BASE + "get_favorite_recipes.php", "user_id=" + MainActivity.getUser().getId());
                    recipes = RecipesUtil.getRecipesFrom(response);
                    Log.w("favorites", recipes.toString());
                    return RecipesFragment.create("Favorites", recipes);
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(RecipesFragment fragment) {

            _progressDialog.dismiss();

            if (fragment != null) {
                if (fragment.getArguments().getSerializable(RecipesFragment.ARG_RECIPES) != null) {
                   ACTIVITY.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
                } else {
                    if (fragment.getTitle().equals("Uploaded"))
                        Toast.makeText(ACTIVITY, "You don't have any uploaded recipes.", Toast.LENGTH_LONG).show();
                    else if (fragment.getTitle().equals("Favorites"))
                        Toast.makeText(ACTIVITY, "You don't have any favorite recipes.", Toast.LENGTH_LONG).show();
                }
            } else
                Toast.makeText(ACTIVITY, R.string.error_message, Toast.LENGTH_SHORT).show();

        }
    }
}
