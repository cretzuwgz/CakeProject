package com.teentitans.cakeproject.utils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

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
    private String ratingCounter;
    private String difficulty;
    private String reqTime;
    private String searched;
    private String accessed;
    private HashMap<String, String> ingredientToQuantity;

    public RecipeVO(String id, String title, String date, String uploader, String description, String pLink, String rating, String ratingCounter, String difficulty, String reqTime, String searched, String accessed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.uploader = uploader;
        this.description = description;
        this.pLink = pLink;
        this.rating = rating;
        this.ratingCounter = ratingCounter;
        this.difficulty = difficulty;
        this.reqTime = reqTime;
        this.searched = searched;
        this.accessed = accessed;
        ingredientToQuantity = new HashMap<String, String>();
    }

    @SuppressWarnings("unchecked")
    public RecipeVO(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.uploader = in.readString();
        this.description = in.readString();
        this.pLink = in.readString();
        this.rating = in.readString();
        this.ratingCounter = in.readString();
        this.difficulty = in.readString();
        this.reqTime = in.readString();
        this.searched = in.readString();
        this.accessed = in.readString();
        this.ingredientToQuantity = (HashMap<String, String>) in.readBundle().getSerializable("map");
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUploader() {
        return uploader;
    }

    public String getDescription() {
        return description;
    }

    public String getpLink() {
        return pLink;
    }

    public String getRating() {
        return rating;
    }

    public String getRatingCounter() {
        return ratingCounter;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getReqTime() {
        return reqTime;
    }

    public String getSearched() {
        return searched;
    }

    public String getAccessed() {
        return accessed;
    }

    public HashMap<String, String> getIngredientToQuantity() {
        return ingredientToQuantity;
    }

    public String addIngredient(String key, String value) {
        return ingredientToQuantity.put(key, value);
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
        parcel.writeString(ratingCounter);
        parcel.writeString(difficulty);
        parcel.writeString(reqTime);
        parcel.writeString(searched);
        parcel.writeString(accessed);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", ingredientToQuantity);
        parcel.writeBundle(bundle);
    }
}
