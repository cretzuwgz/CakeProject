package com.teentitans.cakeproject.utils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RecipeVO implements Parcelable {

    public static final Parcelable.Creator<RecipeVO> CREATOR = new Parcelable.Creator<RecipeVO>() {
        public RecipeVO createFromParcel(Parcel in) {
            return new RecipeVO(in);
        }

        public RecipeVO[] newArray(int size) {
            return new RecipeVO[size];
        }
    };

    private String id;
    private String title;
    private String date;
    private String uploader;
    private String description;
    private String pLink;
    private String rating;
    private int difficulty;
    private String reqTime;
    private ArrayList<IngredientVO> ingredients;
    private ArrayList<String> tags;

    RecipeVO(String id, String title, String date, String uploader, String description, String pLink, String rating, int difficulty, String reqTime) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.uploader = uploader;
        this.description = description;
        this.pLink = pLink;
        this.rating = rating;
        this.difficulty = difficulty;
        this.reqTime = reqTime;
        ingredients = new ArrayList<>();
        tags = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private RecipeVO(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.uploader = in.readString();
        this.description = in.readString();
        this.pLink = in.readString();
        this.rating = in.readString();
        this.difficulty = in.readInt();
        this.reqTime = in.readString();
        Bundle b = in.readBundle(getClass().getClassLoader());
        this.ingredients = (ArrayList<IngredientVO>) b.getSerializable("ingredients");
        this.tags = (ArrayList<String>) b.getSerializable("tags");
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPLink() {
        return pLink;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getReqTime() {
        return reqTime;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getDate() {
        return date;
    }

    public String getUploader() {
        return uploader;
    }

    public ArrayList<IngredientVO> getIngredients() {
        return ingredients;
    }

    String getFirstXTags(int x) {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < x - 1; i++)
            buffer.append(tags.get(i)).append(", ");

        buffer.append(tags.get(x - 1));

        return buffer.toString().trim();
    }

    void addTag(String tag) {
        tags.add(tag);
    }

    void addIngredient(IngredientVO ingredient) {
        ingredients.add(ingredient);
    }

    @Override

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(uploader);
        parcel.writeString(description);
        parcel.writeString(pLink);
        parcel.writeString(rating);
        parcel.writeInt(difficulty);
        parcel.writeString(reqTime);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ingredients", ingredients);
        bundle.putSerializable("tags", tags);
        parcel.writeBundle(bundle);
    }

    public String getTagsAsString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (String tag : tags)
            stringBuilder.append(tag).append(",");

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
