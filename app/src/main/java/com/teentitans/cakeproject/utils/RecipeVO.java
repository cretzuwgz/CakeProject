package com.teentitans.cakeproject.utils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RecipeVO implements Parcelable {

    private static final String INGREDIENTS = "ingredients";
    private static final String TAGS = "tags";
    public static final Parcelable.Creator<RecipeVO> CREATOR = new Parcelable.Creator<RecipeVO>() {
        public RecipeVO createFromParcel(Parcel in) {

            return new RecipeVO(in);
        }

        public RecipeVO[] newArray(int size) {

            return new RecipeVO[size];
        }
    };

    private String _id;
    private String _title;
    private String _date;
    private String _uploader;
    private String _description;
    private String _pLink;
    private String _rating;
    private int _difficulty;
    private String _reqTime;
    private ArrayList<IngredientVO> _ingredients;
    private ArrayList<String> _tags;

    public RecipeVO(String id, String title, String date, String uploader, String description, String pLink, String rating, int difficulty, String reqTime) {
        _id = id;
        _title = title;
        _date = date;
        _uploader = uploader;
        _description = description;
        _pLink = pLink;
        _rating = rating;
        _difficulty = difficulty;
        _reqTime = reqTime;
        _ingredients = new ArrayList<>();
        _tags = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private RecipeVO(Parcel in) {
        _id = in.readString();
        _title = in.readString();
        _date = in.readString();
        _uploader = in.readString();
        _description = in.readString();
        _pLink = in.readString();
        _rating = in.readString();
        _difficulty = in.readInt();
        _reqTime = in.readString();
        Bundle b = in.readBundle(getClass().getClassLoader());
        _ingredients = (ArrayList<IngredientVO>) b.getSerializable(INGREDIENTS);
        _tags = (ArrayList<String>) b.getSerializable(TAGS);
    }

    public String getId() {

        return _id;
    }

    public String getTitle() {

        return _title;
    }

    public String getDescription() {

        return _description;
    }

    public String getPLink() {

        return _pLink;
    }

    public String getRating() {

        return _rating;
    }

    public void setRating(String rating) {

        _rating = rating;
    }

    public int getDifficulty() {

        return _difficulty;
    }

    public String getReqTime() {

        return _reqTime;
    }

    public ArrayList<String> getTags() {

        return _tags;
    }

    public String getDate() {

        return _date;
    }

    public String getUploader() {

        return _uploader;
    }

    public ArrayList<IngredientVO> getIngredients() {

        return _ingredients;
    }

    public String getFirstXTags(int x) {

        return _tags.stream().limit(x).collect(Collectors.joining(", "));
    }

    public void addTag(String tag) {

        _tags.add(tag);
    }

    public void addIngredient(IngredientVO ingredient) {

        _ingredients.add(ingredient);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(_id);
        parcel.writeString(_title);
        parcel.writeString(_date);
        parcel.writeString(_uploader);
        parcel.writeString(_description);
        parcel.writeString(_pLink);
        parcel.writeString(_rating);
        parcel.writeInt(_difficulty);
        parcel.writeString(_reqTime);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INGREDIENTS, _ingredients);
        bundle.putSerializable(TAGS, _tags);
        parcel.writeBundle(bundle);
    }

    public String getTagsAsString() {

        return _tags.stream().collect(Collectors.joining(","));
    }
}
