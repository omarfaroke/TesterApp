package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Until;

public class TopTalent {

    @SerializedName("profile")
    private UserProfile userProfile;

    @SerializedName("Category_id")
    private Integer categoryID;

    private Integer score;

    public TopTalent(UserProfile userProfile, Integer categoryID, Integer score) {
        this.userProfile = userProfile;
        this.categoryID = categoryID;
        this.score = score;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public Integer getScore() {
        return score;
    }
}
