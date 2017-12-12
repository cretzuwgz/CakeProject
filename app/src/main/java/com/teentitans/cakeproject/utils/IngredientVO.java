package com.teentitans.cakeproject.utils;

import java.io.Serializable;

public class IngredientVO implements Serializable {

    private String _name;
    private String _quantity;
    private String _measurement;

    IngredientVO(String name, String quantity, String measurement) {
        _name = name;
        _quantity = quantity;
        _measurement = measurement;
    }

    public String get_name() {
        return _name;
    }

    public String getQuantity() {
        return _quantity;
    }

    public String getMeasurement() {
        return _measurement;
    }
}
