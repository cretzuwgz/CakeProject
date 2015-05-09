package com.teentitans.cakeproject.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.utils.RecipeVO;

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

        EditText title = (EditText) findViewById(R.id.etTitle);
        EditText reqTime = (EditText) findViewById(R.id.etReqTime);
        Spinner difficulty = (Spinner) findViewById(R.id.sDifficulty);
        EditText description = (EditText) findViewById(R.id.etDescription);
        EditText tags = (EditText) findViewById(R.id.etTags);

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
                Toast.makeText(EditRecipeActivity.this, "Update button clicked", Toast.LENGTH_LONG).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditRecipeActivity.this, "Delete button clicked", Toast.LENGTH_LONG).show();
            }
        });

        //TODO finish this

    }
}
