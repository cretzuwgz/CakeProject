package com.teentitans.cakeproject.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.fragments.RecipesFragment;
import com.teentitans.cakeproject.utils.ConnectionUtil;
import com.teentitans.cakeproject.utils.RecipeVO;
import com.teentitans.cakeproject.utils.RecipesUtil;
import com.teentitans.cakeproject.utils.SlidingTabLayout;
import com.teentitans.cakeproject.utils.ZoomOutPageTransformer;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    public static String URL_RECOMMENDED = "http://cakeproject.whostf.com/php/get_recommended.php";
    public static String URL_TOP = "http://cakeproject.whostf.com/php/get_top5.php";
    public static String URL_RECENT = "http://cakeproject.whostf.com/php/get_recent.php";
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Recommended", "Recent", "Top"};
    private ArrayList<RecipeVO> recommendedRecipes;
    private ArrayList<RecipeVO> topRecipes;
    private ArrayList<RecipeVO> recentRecipes;
    private DrawerLayout mDrawerLayout;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView listView = (ListView) findViewById(R.id.left_drawer);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        ArrayList<String> navigationDrawerItems = new ArrayList<>();
        navigationDrawerItems.add("NOT AVAILABLE IN BETA");
        navigationDrawerItems.add("NOT AVAILABLE IN BETA");
        navigationDrawerItems.add("NOT AVAILABLE IN BETA");
        navigationDrawerItems.add("NOT AVAILABLE IN BETA");
        navigationDrawerItems.add("NOT AVAILABLE IN BETA");
        navigationDrawerItems.add("NOT AVAILABLE IN BETA");
        navigationDrawerItems.add("NOT AVAILABLE IN BETA");
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_drawer_list, navigationDrawerItems));
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);

        pager.setPageTransformer(true, new ZoomOutPageTransformer());

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
//        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.primary_text);
            }
        });
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        new GetRecipesTask("recommended").execute();
        new GetRecipesTask("top").execute();
        new GetRecipesTask("recent").execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                mDrawerLayout.closeDrawers();
                return true;
            } else {
                mDrawerLayout.openDrawer(Gravity.START | Gravity.LEFT);
                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    public void setAdapter() {
        if (recommendedRecipes != null && recentRecipes != null && topRecipes != null) {
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            int noOfTabs = 3;
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, noOfTabs);
            pager.setAdapter(adapter);
            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);
            progress.hide();
        }
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

        String param;

        public GetRecipesTask(String parameter) {
            param = parameter;
        }

        protected String doInBackground(String... args) {

            String response;

            try {
                switch (param) {
                    case "recommended": {
                        response = ConnectionUtil.getResponseFromURL(URL_RECOMMENDED);
                        recommendedRecipes = RecipesUtil.getRecipesFrom(response);

                        if (response == null)
                            return "Connection failed";
                        else
                            return response;
                    }
                    case "top": {
                        response = ConnectionUtil.getResponseFromURL(URL_TOP);
                        topRecipes = RecipesUtil.getRecipesFrom(response);

                        if (response == null)
                            return "Connection failed";
                        else
                            return response;
                    }
                    case "recent": {
                        response = ConnectionUtil.getResponseFromURL(URL_RECENT);
                        recentRecipes = RecipesUtil.getRecipesFrom(response);

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
            if (result != null) {
                if (result.equals("Connection failed")) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                } else {
                    setAdapter();
                }
            } else {
                Toast.makeText(MainActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            }

        }
    }
}


