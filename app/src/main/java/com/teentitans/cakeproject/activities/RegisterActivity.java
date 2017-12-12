package com.teentitans.cakeproject.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.fragments.RegisterPageFragment;
import com.teentitans.cakeproject.utils.ZoomOutPageTransformer;

public class RegisterActivity extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 2;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager _viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        PagerAdapter mPagerAdapter;

        // Instantiate a ViewPager and a PagerAdapter.
        _viewPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        _viewPager.setAdapter(mPagerAdapter);
        _viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        _viewPager.setOnTouchListener((view, motionEvent) -> true);
        _viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });
    }

    public void nextStep() {

        _viewPager.setCurrentItem(_viewPager.getCurrentItem() + 1, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * A simple pager adapter that represents 2 {@link RegisterPageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return RegisterPageFragment.create(position + 1);
        }

        @Override
        public int getCount() {

            return NUM_PAGES;
        }
    }
}
