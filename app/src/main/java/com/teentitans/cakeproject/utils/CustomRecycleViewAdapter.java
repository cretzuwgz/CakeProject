package com.teentitans.cakeproject.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.activities.MainActivity;
import com.teentitans.cakeproject.activities.ViewRecipeActivity;

import java.util.ArrayList;

public class CustomRecycleViewAdapter extends RecyclerView.Adapter<CustomRecycleViewAdapter.ViewHolder> {
    private static Context context;
    private ArrayList<RecipeVO> recipes;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomRecycleViewAdapter(ArrayList<RecipeVO> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.recipeTitle.setText(recipes.get(position).getTitle());
        if (recipes.get(position).getTags().size() < 3 && recipes.get(position).getTags().size() != 0)
            holder.lTags.setText(recipes.get(position).getFirstXTags(recipes.get(position).getTags().size()));
        else if (recipes.get(position).getTags().size() != 0)
            holder.lTags.setText(recipes.get(position).getFirstXTags(3));
        Picasso.with(context).load(recipes.get(position).getpLink().replaceAll("\\\\", "")).placeholder(R.drawable.cake_bg).into(holder.recipeImage);
        holder.recipeRating.setRating(Integer.valueOf(recipes.get(position).getRating()));
        holder.itemView.setTag(recipes.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CustomImageView recipeImage;
        public TextView recipeTitle;
        public TextView lTags;
        public RatingBar recipeRating;

        public ViewHolder(View v) {
            super(v);
            recipeImage = (CustomImageView) v.findViewById(R.id.recipeImage);
            recipeTitle = (TextView) v.findViewById(R.id.recipeTitle);
            lTags = (TextView) v.findViewById(R.id.lTags);
            recipeRating = (RatingBar) v.findViewById(R.id.recipeRating);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ViewRecipeActivity.navigate((MainActivity) context, view.findViewById(R.id.recipeImage), (RecipeVO) view.getTag());
        }
    }

}