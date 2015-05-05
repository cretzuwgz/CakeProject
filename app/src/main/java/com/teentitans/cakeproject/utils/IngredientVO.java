package com.teentitans.cakeproject.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientVO implements Parcelable {

    public static final Parcelable.Creator<IngredientVO> CREATOR = new Parcelable.Creator<IngredientVO>() {
        public IngredientVO createFromParcel(Parcel in) {
            return new IngredientVO(in);
        }

        public IngredientVO[] newArray(int size) {
            return new IngredientVO[size];
        }
    };

    private String name;
    private String quantity;
    private String measurement;

    public IngredientVO(String name, String quantity, String measurement) {
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
    }

    public IngredientVO(Parcel in) {
        this.name = in.readString();
        this.quantity = in.readString();
        this.measurement = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(quantity);
        dest.writeString(measurement);

    }
}
