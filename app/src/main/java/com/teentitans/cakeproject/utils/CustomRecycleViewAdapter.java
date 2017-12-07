package com.teentitans.cakeproject.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.activities.ViewRecipeActivity;

import java.util.ArrayList;

public class CustomRecycleViewAdapter extends RecyclerView.Adapter<CustomRecycleViewAdapter.ViewHolder> {
    private AppCompatActivity parentActivity;
    private ArrayList<RecipeVO> recipes;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomRecycleViewAdapter(ArrayList<RecipeVO> recipes, AppCompatActivity parent) {
        this.recipes = recipes;
        this.parentActivity = parent;
    }

    public void updateList(ArrayList<RecipeVO> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v, parentActivity);
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
        Picasso.with(parentActivity).load(recipes.get(position).getPLink().replaceAll("\\\\", "")).placeholder(R.drawable.img_placeholder).into(holder.recipeImage);
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
        CustomImageView recipeImage;
        TextView recipeTitle;
        TextView lTags;
        RatingBar recipeRating;
        AppCompatActivity parent;

        ViewHolder(View v, AppCompatActivity parent) {
            super(v);
            recipeImage = v.findViewById(R.id.recipeImage);
            recipeTitle = v.findViewById(R.id.recipeTitle);
            lTags = v.findViewById(R.id.lTags);
            recipeRating = v.findViewById(R.id.recipeRating);
            this.parent = parent;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ViewRecipeActivity.navigate(parent, view.findViewById(R.id.recipeImage), (RecipeVO) view.getTag());
        }
    }

}