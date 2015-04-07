package com.teentitans.cakeproject.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.fragments.RecipesFragment;
import com.teentitans.cakeproject.utils.ConnectionUtil;
import com.teentitans.cakeproject.utils.RecipeVO;
import com.teentitans.cakeproject.utils.SlidingTabLayout;
import com.teentitans.cakeproject.utils.ZoomOutPageTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    public static String URL_RECOMMENDED = "http://cakeproject.whostf.com/php/get_recommended.php";
    public static String URL_TOP = "http://cakeproject.whostf.com/php/get_top5.php";
    public static String URL_RECENT = "http://cakeproject.whostf.com/php/get_recent.php";
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Recommended", "Recent", "Top", "Search"};
    private int noOfTabs = 4;
    private ArrayList<RecipeVO> recommendedRecipes;
    private ArrayList<RecipeVO> topRecipes;
    private ArrayList<RecipeVO> recentRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);

        pager.setPageTransformer(true, new ZoomOutPageTransformer());

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.primary_text);
            }
        });

        new GetRecipesTask().execute("recommended");
        new GetRecipesTask().execute("top");
        new GetRecipesTask().execute("recent");

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

    private ArrayList<RecipeVO> getRecipesFrom(String response) {
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
                recipeVO.addTag("tag1");
                recipeVO.addTag("tag2");
                recipeVO.addTag("tag3");
                recipeList.add(recipeVO);
            }

        } catch (JSONException e) {
            return null;
        }
        return recipeList;
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int noOfTabs) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = noOfTabs;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return RecipesFragment.create(getPageTitle(position).toString(), recommendedRecipes);
                }
                case 1: {
                    return RecipesFragment.create(getPageTitle(position).toString(), recentRecipes);
                }
                case 2: {
                    return RecipesFragment.create(getPageTitle(position).toString(), topRecipes);
                }
                case 3: {
                    return RecipesFragment.create(getPageTitle(position).toString(), null);
                }
            }
            return null;
        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }

    private class GetRecipesTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {

            String response;

            try {
                switch (args[0]) {
                    case "recommended": {
                        Log.e("async", "recommended");
                        response = ConnectionUtil.getResponseFromURL(URL_RECOMMENDED);
                        recommendedRecipes = getRecipesFrom(response);

                        if (response == null)
                            return "Connection failed";
                        else
                            return response;
                    }
                    case "top": {
                        Log.e("async", "top");
                        response = ConnectionUtil.getResponseFromURL(URL_TOP);
                        topRecipes = getRecipesFrom(response);

                        if (response == null)
                            return "Connection failed";
                        else
                            return response;
                    }
                    case "recent": {
                        Log.e("async", "recent");
                        response = ConnectionUtil.getResponseFromURL(URL_RECENT);
                        recentRecipes = getRecipesFrom(response);

                        if (response == null)
                            return "Connection failed";
                        else
                            return response;
                    }
                }
            } catch (IOException e) {
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, noOfTabs);
            pager.setAdapter(adapter);

            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);
            if (result != null) {
                Log.e("result", result);
                if (result.equals("Connection failed"))
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(MainActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
        }
    }
}


