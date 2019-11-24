package com.codingacademy.testerapp.model;

import android.graphics.drawable.Drawable;

import com.codingacademy.testerapp.Constants;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Category {
    @SerializedName("Category_id")
    public Integer catId;

    @SerializedName("Category_name")
    public String name;

    @SerializedName("parent")
    public Integer parentId;

    @SerializedName("image_url")
    public String imageURL;

    @SerializedName("status")
    public Integer stutus;


    public Category(Integer catId, int parentId, String name) {

        this.catId = catId;
        this.parentId = parentId;
        this.name = name;
    }

    public Category(Integer catId, String name, Integer parentId, String imageURL, Integer stutus) {
        this.catId = catId;
        this.name = name;
        this.parentId = parentId;
        this.imageURL = imageURL;
        this.stutus = stutus;
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
