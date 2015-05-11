package com.teentitans.cakeproject.activities;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import com.teentitans.cakeproject.utils.RecipeVO;

import java.io.IOException;

public class EditRecipeActivity extends ActionBarActivity {

    private RecipeVO recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        recipe = getIntent().getBundleExtra("bundle").getParcelable("recipe");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final EditText title = (EditText) findViewById(R.id.etTitle);
        final EditText reqTime = (EditText) findViewById(R.id.etReqTime);
        final Spinner difficulty = (Spinner) findViewById(R.id.sDifficulty);
        final EditText description = (EditText) findViewById(R.id.etDescription);
        final EditText tags = (EditText) findViewById(R.id.etTags);

        Button update = (Button) findViewById(R.id.btnUpdateRecipe);
        Button delete = (Button) findViewById(R.id.btnDeleteRecipe);

        title.setText(recipe.getTitle());
        reqTime.setText(recipe.getReqTime());
        difficulty.setSelection(recipe.getDifficulty() - 1);
        description.setText(recipe.getDescription());
        tags.setText(recipe.getTagsAsString());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateRecipeTask().execute(recipe.getId(), title.getText().toString(), reqTime.getText().toString(), String.valueOf(difficulty.getSelectedItemPosition() + 1), description.getText().toString(), tags.getText().toString());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditRecipeActivity.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteRecipeTask().execute(recipe.getId());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setTitle("Are you sure you want to delete this recipe?");
                AlertDialog dialog = builder.create();
                dialog.show();
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
                Intent intent = new Intent(EditRecipeActivity.this, SearchActivity.class);
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

    public class UpdateRecipeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String parameters = "recipeId=" + params[0] + "&title=" + params[1] + "&reqTime=" + params[2] + "&difficulty=" + params[3] + "&description=" + params[4] + "&tags=" + params[5];
            String response = null;
            try {
                response = ConnectionUtil.getResponseFromURL("http://cakeproject.whostf.com/php/update_recipe.php", parameters);//TODO php
            } catch (IOException e) {
                Log.e("Update Recipe", "error");
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null)
                Toast.makeText(EditRecipeActivity.this, R.string.error_message, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(EditRecipeActivity.this, response, Toast.LENGTH_LONG).show();
        }
    }

    public class DeleteRecipeTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                ConnectionUtil.getResponseFromURL("http://cakeproject.whostf.com/php/delete_recipe.php", "recipeId=" + params[0]);//TODO php
            } catch (IOException e) {
                Log.e("Delete Recipe", "error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            finish();
        }
    }
}
