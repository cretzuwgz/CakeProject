package com.teentitans.cakeproject.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.teentitans.cakeproject.utils.UserVO;
import com.teentitans.cakeproject.utils.ZoomOutPageTransformer;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String URL_RECOMMENDED = "http://cakeproject.whostf.com/php/get_recommended.php";
    public static String URL_TOP = "http://cakeproject.whostf.com/php/get_top5.php";
    public static String URL_RECENT = "http://cakeproject.whostf.com/php/get_recent.php";
    private static ArrayList<RecipeVO> recommendedRecipes;
    private static ArrayList<RecipeVO> topRecipes;
    private static ArrayList<RecipeVO> recentRecipes;
    private static UserVO user;
    private static boolean toUpdate = true;
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Recommended", "Recent", "Top"};
    private ProgressDialog progress;
    private ActionBarDrawerToggle mDrawerToggle;

    public static UserVO getUser() {
        return user;
    }

    public static void setToUpdate() {
        toUpdate = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        ListView listView = findViewById(R.id.left_drawer);

        if (getIntent().getBundleExtra("bundle") != null)
            user = getIntent().getBundleExtra("bundle").getParcelable("user");

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        ArrayList<String> navigationDrawerItems = new ArrayList<>();
        navigationDrawerItems.add("Settings");
        navigationDrawerItems.add("Uploads");
        navigationDrawerItems.add("Favorites");
        navigationDrawerItems.add("Upload new recipe");
        navigationDrawerItems.add("Logout");

        listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_drawer_list, navigationDrawerItems));

        listView.setClickable(true);
        listView.setOnItemClickListener((arg0, arg1, position, arg3) -> {

                    if (user.isGuest()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setPositiveButton("Yes", (dialog, id) -> {
                            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                        builder.setNegativeButton("No", (dialogInterface, i) -> {
                        });
                        builder.setTitle("Do you want to register?");
                        builder.setMessage("Side menu is available only for registered users");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else
                        switch (position) {
                            case 0: {
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case 1: {
                                Intent intent = new Intent(MainActivity.this, RecipesListActivity.class);
                                intent.putExtra("activityFor", "uploaded");
                                startActivity(intent);
                                break;
                            }
                            case 2: {
                                Intent intent = new Intent(MainActivity.this, RecipesListActivity.class);
                                intent.putExtra("activityFor", "favorites");
                                startActivity(intent);
                                break;
                            }
                            case 3: {
                                Intent intent = new Intent(MainActivity.this, NewRecipeActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case 4: {
                                finish();
                                break;
                            }
                            default:
                                break;
                        }
                }
        );

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Assigning ViewPager View and setting the adapter
        pager = findViewById(R.id.pager);

        pager.setPageTransformer(true, new ZoomOutPageTransformer());

        // Assigning the Sliding Tab Layout View
        tabs = findViewById(R.id.tabs);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(position -> getResources().getColor(R.color.primary_text));
        progress = new ProgressDialog(this);

        if (needsUpdate())
            updateData();
        else setAdapter();
    }

    private boolean needsUpdate() {
        if (user.isGuest())
            return recentRecipes == null || toUpdate;

        return recentRecipes == null || recommendedRecipes == null || topRecipes == null || toUpdate;
    }

    private void updateData() {
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        if (!user.isGuest()) {
            new GetRecipesTask().execute("recommended");
            new GetRecipesTask().execute("top");
        }
        new GetRecipesTask().execute("recent");

        toUpdate = false;
    }

    @Override
    protected void onDestroy() {
        recentRecipes = null;
        recommendedRecipes = null;
        topRecipes = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (user.isGuest())
            getMenuInflater().inflate(R.menu.menu_guest, menu);
        else {
            getMenuInflater().inflate(R.menu.menu_main, menu);

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView searchView =
                    (SearchView) menu.findItem(R.id.action_search).getActionView();
            if (searchManager != null) {
                searchView.setSearchableInfo(
                        searchManager.getSearchableInfo(getComponentName()));
            }

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
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
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mDrawerToggle.onOptionsItemSelected(item);

//        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needsUpdate())
            updateData();
        else
            setAdapter();
    }

    public void setAdapter() {
        if (user.isGuest()) {
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            int noOfTabs = 1;
            CharSequence x[] = {"Recent"};
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), x, noOfTabs);
            pager.setAdapter(adapter);
            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);
            progress.dismiss();
        } else if (recommendedRecipes != null && recentRecipes != null && topRecipes != null) {
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            int noOfTabs = 3;
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, noOfTabs);
            pager.setAdapter(adapter);
            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);
            progress.dismiss();
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


        // Build a Constructor and assign the passed Values to appropriate values in the class
        ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int noOfTabs) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = noOfTabs;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {
            CharSequence pageTitle = getPageTitle(position);
            if (pageTitle == null)
                return null;

            if (user.isGuest())
                return RecipesFragment.create(pageTitle.toString(), recentRecipes);

            switch (position) {
                case 0:
                    return RecipesFragment.create(pageTitle.toString(), recommendedRecipes);
                case 1:
                    return RecipesFragment.create(pageTitle.toString(), recentRecipes);
                case 2:
                    return RecipesFragment.create(pageTitle.toString(), topRecipes);
                default:
                    return null;
            }
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

    private class GetRecipesTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... args) {

            String response;

            try {
                switch (args[0]) {
                    case "recommended": {
                        response = ConnectionUtil.getResponseFromURL(URL_RECOMMENDED, "user_id=" + user.getId());
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


