package com.codingacademy.testerapp.model;

import com.codingacademy.testerapp.Constants;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Category {
    @SerializedName("Category_id")
    public Integer catId;

    @SerializedName("Category_name")
    public String name;

    @SerializedName("parent")
    public Integer parentId;

    @SerializedName("image_url")
    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    @SerializedName("status")
    private Integer status;


    public Category(Integer catId, int parentId, String name) {

        this.catId = catId;
        this.parentId = parentId;
        this.name = name;
    }

    public Category(Integer catId, String name, Integer parentId, String imageURL, Integer status) {
        this.catId = catId;
        this.name = name;
        this.parentId = parentId;
        this.imageURL = imageURL;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
