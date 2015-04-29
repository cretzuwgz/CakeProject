package com.teentitans.cakeproject.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipesUtil {

    public static ArrayList<RecipeVO> getRecipesFrom(String response) {
        JSONArray recipesJson;
        ArrayList<RecipeVO> recipeList = new ArrayList<>();

        try {
            recipesJson = new JSONObject(response).getJSONArray("recipes");
        } catch (JSONException e) {
            return null;
        }
        try {
            for (int i = 0; i < recipesJson.length(); i++) {
                JSONObject recipe = recipesJson.getJSONObject(i);
                RecipeVO recipeVO = new RecipeVO(recipe.getString("id"), recipe.getString("title"), recipe.getString("date"), recipe.getString("uploader"), recipe.getString("description"), recipe.getString("p_link"), recipe.getString("rating"), recipe.getString("difficulty"), recipe.getString("req_time"));
                for (int j = 0; j < recipe.getJSONArray("tags").length(); j++)
                    recipeVO.addTag(recipe.getJSONArray("tags").getString(j));
                recipeList.add(recipeVO);

            }

        } catch (JSONException e) {
            return null;
        }
        return recipeList;
    }
}
