package com.teentitans.cakeproject.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class UserVO implements Parcelable{
    
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
    private String date;
    private String gender;
    private String experience;

    public UserVO(String id, String username, String date, String gender, String experience) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.gender = gender;
        this.experience = experience;
    }

    private UserVO(Parcel in) {
        id = in.readString();
        username = in.readString();
        date = in.readString();
        gender = in.readString();
        experience = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getGender() {
        return gender;
    }

    public String getExperience() {
        return experience;
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
        parcel.writeString(gender);
        parcel.writeString(experience);
    }
}
