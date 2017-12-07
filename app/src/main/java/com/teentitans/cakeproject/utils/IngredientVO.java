package com.teentitans.cakeproject.utils;

import java.io.Serializable;

public class IngredientVO implements Serializable {

    private String name;
    private String quantity;
    private String measurement;

    IngredientVO(String name, String quantity, String measurement) {
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
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
}
