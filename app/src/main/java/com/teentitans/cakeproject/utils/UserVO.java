package com.teentitans.cakeproject.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserVO implements Parcelable {

    public static final Parcelable.Creator<UserVO> CREATOR = new Parcelable.Creator<UserVO>() {
        public UserVO createFromParcel(Parcel in) {
            return new UserVO(in);
        }

        public UserVO[] newArray(int size) {
            return new UserVO[size];
        }
    };

    private String id;
    private String username;
    private String password;
    private String date;
    private int gender;
    private int experience;
    private ArrayList<String> favoriteTags;

    public UserVO(String id, String username, String password, String date, int gender, int experience) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.date = date;
        this.gender = gender;
        this.experience = experience;
        favoriteTags = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private UserVO(Parcel in) {
        id = in.readString();
        username = in.readString();
        date = in.readString();
        gender = in.readInt();
        experience = in.readInt();
        favoriteTags = (ArrayList<String>) in.readSerializable();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDate() {
        return date;
    }

    public int getGender() {
        return gender;
    }

    public int getExperience() {
        return experience;
    }

    public ArrayList<String> getFavoriteTags() {
        return favoriteTags;
    }

    public String getFavoriteTagsAsString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (String tag : favoriteTags)
            stringBuilder.append(tag).append(",");

        if (!stringBuilder.toString().isEmpty())
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public void addTag(String tag) {
        favoriteTags.add(tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(username);
        parcel.writeString(date);
        parcel.writeInt(gender);
        parcel.writeInt(experience);
        parcel.writeSerializable(favoriteTags);
    }
}
