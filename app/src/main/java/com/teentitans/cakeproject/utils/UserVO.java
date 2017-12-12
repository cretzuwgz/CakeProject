package com.teentitans.cakeproject.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserVO implements Parcelable {

    public static final Parcelable.Creator<UserVO> CREATOR = new Parcelable.Creator<UserVO>() {
        public UserVO createFromParcel(Parcel in) {

            return new UserVO(in);
        }

        public UserVO[] newArray(int size) {

            return new UserVO[size];
        }
    };

    private String _id;
    private String _username;
    private String _password;
    private String _date;
    private int _gender;
    private int _experience;
    private Boolean _isGuest;
    private ArrayList<String> _favoriteTags;

    public UserVO(String id, String username, String password, String date, int gender, int experience, Boolean isGuest) {

        _id = id;
        _username = username;
        _password = password;
        _date = date;
        _gender = gender;
        _experience = experience;
        _isGuest = isGuest;
        _favoriteTags = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private UserVO(Parcel in) {

        _id = in.readString();
        _username = in.readString();
        _date = in.readString();
        _gender = in.readInt();
        _experience = in.readInt();
        _isGuest = (Boolean) in.readValue(getClass().getClassLoader());
        _favoriteTags = (ArrayList<String>) in.readSerializable();
    }

    public String getId() {

        return _id;
    }

    public void setId(String id) {

        _id = id;
    }

    public String getUsername() {

        return _username;
    }

    public String getPassword() {

        return _password;
    }

    public String getDate() {

        return _date;
    }

    public int getGender() {

        return _gender;
    }

    public int getExperience() {

        return _experience;
    }

    public Boolean isGuest() {

        return _isGuest;
    }

    public ArrayList<String> getFavoriteTags() {

        return _favoriteTags;
    }

    public String getFavoriteTagsAsString() {

        return _favoriteTags.stream().collect(Collectors.joining(","));
    }

    public void addTag(String tag) {

        _favoriteTags.add(tag);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(_id);
        parcel.writeString(_username);
        parcel.writeString(_date);
        parcel.writeInt(_gender);
        parcel.writeInt(_experience);
        parcel.writeValue(_isGuest);
        parcel.writeSerializable(_favoriteTags);
    }
}
