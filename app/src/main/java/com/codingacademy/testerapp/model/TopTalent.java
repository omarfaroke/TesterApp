package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Until;

public class TopTalent {

    @SerializedName("profile")
    private UserProfile userProfile;

    @SerializedName("Category_id")
    private Integer categoryID;

    @SerializedName("exam_id")
    private Integer examId;

    @SerializedName("sample_id")
    private Integer sampleId;

    @SerializedName("date")
    private String date;

    private Integer score;

    public TopTalent(UserProfile userProfile, Integer categoryID, Integer score) {
        this.userProfile = userProfile;
        this.categoryID = categoryID;
        this.score = score;
    }

    public TopTalent(UserProfile userProfile, Integer categoryID, Integer examId, Integer sampleId, String date, Integer score) {
        this.userProfile = userProfile;
        this.categoryID = categoryID;
        this.examId = examId;
        this.sampleId = sampleId;
        this.date = date;
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
