package com.codingacademy.testerapp.model;

import android.graphics.drawable.Drawable;

import com.codingacademy.testerapp.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Category {
    public Integer catId,parentId,stutus;

    public String name;

    public Category(int catId, int parentId, String name) {
        this.catId = catId;
        this.parentId = parentId;
        this.name = name;
    }


    public Category(JSONObject jsonCat) {
        try {
            this.catId = jsonCat.getInt(Constants.CATEGORY_ID);
            this.parentId = jsonCat.getInt(Constants.CATEGORY_PARENT);
            this.name = jsonCat.getString(Constants.CATEGORY_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getCatId() {
        return catId;
    }

    public int getParentId() {
        return parentId;
    }

    public int getStutus() {
        return stutus;
    }

    public String getName() {
        return name;
    }
}
