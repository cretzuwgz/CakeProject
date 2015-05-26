package com.teentitans.cakeproject.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
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

public class NewRecipeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final EditText etTitle = (EditText) findViewById(R.id.etTitle);
        final EditText etPicture = (EditText) findViewById(R.id.etPicture);
        final EditText etReqTime = (EditText) findViewById(R.id.etReqTime);
        final Spinner sDifficulty = (Spinner) findViewById(R.id.sDifficulty);
        final EditText etIngredients = (EditText) findViewById(R.id.etIngredients);
        final EditText etDescription = (EditText) findViewById(R.id.etDescription);
        final EditText etTags = (EditText) findViewById(R.id.etTags);

        Button btnAdd = (Button) findViewById(R.id.btnAddRecipe);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = etTitle.getText().toString();
                String pictureLink = etPicture.getText().toString();
                String reqTime = etReqTime.getText().toString();
                String difficulty = String.valueOf(sDifficulty.getSelectedItemPosition() + 1);
                String ingredients = etIngredients.getText().toString();
                String description = etDescription.getText().toString();
                String tags = etTags.getText().toString();

                if (pictureLink.isEmpty())
                    pictureLink = "NULL";

                if (title.isEmpty() || reqTime.isEmpty() || difficulty.isEmpty() || ingredients.isEmpty() || description.isEmpty() || tags.isEmpty())
                    Toast.makeText(NewRecipeActivity.this, "Please complete all the required fields", Toast.LENGTH_LONG).show();
                else if (!ingredients.matches("(\\w+:\\w+:\\w+,\\s?)*(\\w+:\\w+:\\w+)"))
                    Toast.makeText(NewRecipeActivity.this, "Please put the ingredients in the correct format (ingredient:quantity:measurement)", Toast.LENGTH_LONG).show();
                else {
                    new AddRecipeTask().execute(title, MainActivity.getUser().getId(), pictureLink, reqTime, difficulty, ingredients, description, tags);
                }
            }
        });

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
                Intent intent = new Intent(NewRecipeActivity.this, SearchActivity.class);
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

    public class AddRecipeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String parameters = "title=" + params[0] + "&uploader=" + params[1] + "&p_link=" + params[2] + "&req_time=" + params[3] + "&difficulty=" + params[4] + "&ingredients=" + params[5] + "&description=" + params[6] + "&tags=" + params[7];

            String response = null;
            try {
                response = ConnectionUtil.getResponseFromURL("http://cakeproject.whostf.com/php/add_recipe.php", parameters);
            } catch (IOException e) {
                Log.e("Add Recipe", "error");
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null)
                Toast.makeText(NewRecipeActivity.this, R.string.error_message, Toast.LENGTH_LONG).show();
            else if (response.equals("OK")) {
                MainActivity.setToUpdate();
                Intent intent = new Intent(NewRecipeActivity.this, RecipesListActivity.class);
                intent.putExtra("activityFor", "uploaded");
                startActivity(intent);
            } else
                Toast.makeText(NewRecipeActivity.this, R.string.error_message, Toast.LENGTH_LONG).show();
        }
    }
}
