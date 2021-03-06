package com.teentitans.cakeproject.activities;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.utils.ConnectionUtil;
import com.teentitans.cakeproject.utils.RecipeVO;

import java.io.IOException;

public class EditRecipeActivity extends AppCompatActivity{

    private static EditRecipeActivity ACTIVITY;
    private RecipeVO recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY = this;
        setContentView(R.layout.activity_edit_recipe);

        recipe = getIntent().getBundleExtra("bundle").getParcelable("recipe");

        Toolbar toolbar = findViewById(R.id.toolbar);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final EditText title = findViewById(R.id.etTitle);
        final EditText reqTime = findViewById(R.id.etReqTime);
        final Spinner difficulty = findViewById(R.id.sDifficulty);
        final EditText description = findViewById(R.id.etDescription);
        final EditText tags = findViewById(R.id.etTags);

        Button update = findViewById(R.id.btnUpdateRecipe);
        Button delete = findViewById(R.id.btnDeleteRecipe);

        title.setText(recipe.getTitle());
        reqTime.setText(recipe.getReqTime());
        difficulty.setSelection(recipe.getDifficulty() - 1);
        description.setText(recipe.getDescription());
        tags.setText(recipe.getTagsAsString());

        update.setOnClickListener(v -> new UpdateRecipeTask().execute(recipe.getId(), title.getText().toString(), reqTime.getText().toString(), String.valueOf(difficulty.getSelectedItemPosition() + 1), description.getText().toString().replaceAll(" ", "%20"), tags.getText().toString().replaceAll(" ", "%20")));

        delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditRecipeActivity.this);
            builder.setPositiveButton("Yes", (dialog, id) -> new DeleteRecipeTask().execute(recipe.getId()));
            builder.setNegativeButton("No", (dialogInterface, i) -> {
            });
            builder.setTitle("Are you sure you want to delete this recipe?");
            AlertDialog dialog = builder.create();
            dialog.show();
        });
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

    private static class UpdateRecipeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String parameters = "recipe_id=" + params[0] + "&title=" + params[1] + "&req_time=" + params[2] + "&difficulty=" + params[3] + "&description=" + params[4] + "&tags=" + params[5];
            String response = null;
            try {
                response = ConnectionUtil.getResponseFromURL(ConnectionUtil.URL_BASE + "update_recipe.php", parameters);
            } catch (IOException e) {
                Log.e("Update Recipe", "error");
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null)
                Toast.makeText(ACTIVITY, R.string.error_message, Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(ACTIVITY, response, Toast.LENGTH_LONG).show();
                MainActivity.setToUpdate();
            }
        }
    }

    private static class DeleteRecipeTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                ConnectionUtil.getResponseFromURL(ConnectionUtil.URL_BASE + "delete_recipe.php", "recipe_id=" + params[0]);
            } catch (IOException e) {
                Log.e("Delete Recipe", "error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity.setToUpdate();
            Intent intent = new Intent(ACTIVITY, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
            ACTIVITY.startActivity(intent);
        }
    }
}
