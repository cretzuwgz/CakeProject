package com.teentitans.cakeproject.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.Picasso;
import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.utils.ConnectionUtil;
import com.teentitans.cakeproject.utils.IngredientVO;
import com.teentitans.cakeproject.utils.RecipeVO;

import java.io.IOException;

public class ViewRecipeActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final boolean TOOLBAR_IS_STICKY = true;
    private RecipeVO recipe;
    private View mToolbar;
    private View mImageView;
    private View mOverlayView;
    private View mTextOverlayView;
    private TextView mTitleView;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private int mToolbarColor;
    private boolean mFabIsShown;
    private boolean isViewedByUploader;
    private Button btnRate;

    //TODO: remove from favorites


    public static void navigate(AppCompatActivity activity, View transitionImage, RecipeVO recipe) {
        Intent intent = new Intent(activity, ViewRecipeActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("recipe", recipe);
        intent.putExtra("bundle", b);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, "image");
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipe = getIntent().getBundleExtra("bundle").getParcelable("recipe");

        isViewedByUploader = MainActivity.getUser().getUsername().equals(recipe.getUploader());

        initActivityTransitions();
        ActivityCompat.postponeEnterTransition(this);
        setContentView(R.layout.activity_view_recipe);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar((Toolbar) mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new IncrementViewCounter().execute(recipe.getId());

        final RatingBar ratingBar = findViewById(R.id.ratingBar);

        ratingBar.setRating(Float.valueOf(recipe.getRating()));

        ((Toolbar) mToolbar).setNavigationOnClickListener(v -> onBackPressed());

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();
        mToolbarColor = getResources().getColor(R.color.primary);

        mImageView = findViewById(R.id.image);

        final ImageView image = (ImageView) mImageView;
        ViewCompat.setTransitionName(image, "image");
        Picasso.with(this).load(recipe.getPLink()).placeholder(R.drawable.img_placeholder).into(image);

        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        mTextOverlayView = findViewById(R.id.overlay_text);

        ObservableScrollView mScrollView = findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView = findViewById(R.id.title);
        mTitleView.setText(recipe.getTitle());
        setTitle(null);

        TextView tvDescription = findViewById(R.id.description);
        tvDescription.setText(recipe.getDescription());

        TextView tvTime = findViewById(R.id.time);
        tvTime.setText(getString(R.string.req_time).replace("{{time}}", recipe.getReqTime()));

        TextView tvIngredients = findViewById(R.id.ingredients);

        for (IngredientVO ingredient : recipe.getIngredients()) {
            tvIngredients.append(ingredient.getName() + ": " + ingredient.getQuantity() + " " + ingredient.getMeasurement() + "\n");
        }

        TextView tvExperience = findViewById(R.id.experience);

        switch (recipe.getDifficulty()) {
            case 1: {
                tvExperience.setText(R.string.beginner);
                break;
            }
            case 2: {
                tvExperience.setText(R.string.medium);
                break;
            }
            case 3: {
                tvExperience.setText(R.string.advanced);
                break;
            }
            default: {
                tvExperience.setText(R.string.unknown);
                break;
            }
        }

        mFab = findViewById(R.id.fab);
        if (isViewedByUploader) {
            ((FloatingActionButton) mFab).setColorNormal(Color.YELLOW);
            ((FloatingActionButton) mFab).setImageResource(android.R.drawable.ic_menu_edit);
        }
        mFab.setOnClickListener(v -> {
            if (MainActivity.getUser().isGuest()) {
                showRegisterAlertDialog();
            } else if (isViewedByUploader) {
                Intent intent = new Intent(ViewRecipeActivity.this, EditRecipeActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("recipe", recipe);
                intent.putExtra("bundle", b);
                startActivity(intent);
            } else {
                new AddToFavorites().execute(recipe.getId(), MainActivity.getUser().getId());
                Toast.makeText(ViewRecipeActivity.this, "Recipe added to favorites", Toast.LENGTH_SHORT).show();
            }
        });

        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, () -> onScrollChanged(0, false, false));

        btnRate = findViewById(R.id.btnRate);
        btnRate.setOnClickListener(v -> {
            if (MainActivity.getUser().isGuest()) {
                showRegisterAlertDialog();
            } else {
                btnRate.setEnabled(false);
                new RatingTask().execute(recipe.getId(), String.valueOf(ratingBar.getRating()));
            }
        });
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void showRegisterAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewRecipeActivity.this);
        builder.setPositiveButton("Yes", (dialog, id) -> {
            Intent intent = new Intent(ViewRecipeActivity.this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
        });
        builder.setTitle("Do you want to register?");
        builder.setMessage("This feature is available only for registered users");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        ViewHelper.setAlpha(mTextOverlayView, ScrollUtils.getFloat(1 - (float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationX = scrollY / 3;
        int titleTranslationY = maxTitleTranslationY - scrollY;

        if (TOOLBAR_IS_STICKY) {
            titleTranslationX = Math.min(mTitleView.getHeight(), titleTranslationX);
            titleTranslationY = Math.max(0, titleTranslationY);
        }
        ViewHelper.setTranslationX(mTitleView, titleTranslationX);
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);
        ViewHelper.setTranslationY(mTextOverlayView, titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
        ViewHelper.setTranslationY(mFab, fabTranslationY);

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }

        if (TOOLBAR_IS_STICKY) {
            // Change alpha of toolbar background
            if (-scrollY + mFlexibleSpaceImageHeight <= mActionBarSize) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolbarColor));
            } else {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
            }
        } else {
            // Translate Toolbar
            if (scrollY < mFlexibleSpaceImageHeight) {
                ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                ViewHelper.setTranslationY(mToolbar, -scrollY);
            }
        }

    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

    private class IncrementViewCounter extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                ConnectionUtil.getResponseFromURL("https://cakeproject.000webhostapp.com/php/viewed.php", "recipe_id=" + params[0]);
            } catch (IOException e) {
                Log.e("Increment View Counter", "error");
            }
            return null;
        }
    }

    private class RatingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                String response = ConnectionUtil.getResponseFromURL("https://cakeproject.000webhostapp.com/php/add_rating.php", "recipe_id=" + params[0] + "&rating=" + params[1]);
                recipe.setRating(response);
            } catch (IOException e) {
                Log.e("Rating", "error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ViewRecipeActivity.this, "Thank you", Toast.LENGTH_LONG).show();
            btnRate.setVisibility(View.GONE);
            MainActivity.setToUpdate();
        }
    }

    private class AddToFavorites extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                String response = ConnectionUtil.getResponseFromURL("https://cakeproject.000webhostapp.com/php/add_favorite.php", "recipe_id=" + params[0] + "&user_id=" + params[1]);
                recipe.setRating(response);
            } catch (IOException e) {
                Log.e("Favorite", "error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ViewRecipeActivity.this, "Thank you", Toast.LENGTH_LONG).show();
        }
    }
}