package com.teentitans.cakeproject.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class RecipesUtil {

    public static ArrayList<RecipeVO> getRecipesFrom(String response) {

        JSONArray recipesJson;
        ArrayList<RecipeVO> recipeList = new ArrayList<>();

        try {
            recipesJson = new JSONObject(response).getJSONArray("recipes");
        } catch (JSONException e) {
            Log.e("RecipesUtil", "Unable to parse response: " + response + "\n" + e);
            return recipeList;
        }

        for (int i = 0; i < recipesJson.length(); i++) {
            JSONObject recipe = null;
            try {
                recipe = recipesJson.getJSONObject(i);
                int difficulty = recipe.getString("difficulty").equals("null") ? 0 : Integer.valueOf(recipe.getString("difficulty"));
                RecipeVO recipeVO = new RecipeVO(recipe.getString("id"), recipe.getString("title"), recipe.getString("date"), recipe.getString("uploader"), recipe.getString("description"), recipe.getString("p_link"), recipe.getString("rating"), difficulty, recipe.getString("req_time"));

                JSONArray array = recipe.getJSONArray("tags");

                for (int j = 0; j < array.length(); j++)
                    recipeVO.addTag(array.getString(j));

                array = recipe.getJSONArray("ingredients");

                for (int j = 0; j < array.length(); j++) {
                    JSONObject jsonIngredient = array.getJSONObject(j);
                    IngredientVO currentIngredient = new IngredientVO(jsonIngredient.getString("name"), jsonIngredient.getString("quantity"), jsonIngredient.getString("unit"));
                    recipeVO.addIngredient(currentIngredient);
                }
                recipeList.add(recipeVO);
            } catch (JSONException e) {
                Log.e("RecipesUtil", "Unable to extract recipe from: " + recipe + "\n" + e);
            }
        }
        return recipeList;
    }
}
