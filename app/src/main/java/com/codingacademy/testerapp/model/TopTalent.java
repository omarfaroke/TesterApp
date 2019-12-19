package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Until;

import java.io.Serializable;

public class TopTalent implements Serializable {

    @SerializedName("profile")
    private UserProfile userProfile;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("status_user")
    private Integer mStatusUser;

    @SerializedName("sample")
    private Sample mSample;

    @SerializedName("exam")
    private Exam mExam;

    @SerializedName("Category_id")
    private Integer categoryID;

    @SerializedName("date")
    private String date;

    @SerializedName("status")
    private Integer status;

    private Integer score;

    public TopTalent(){

    }

    public TopTalent(UserProfile userProfile, Integer categoryID, Integer score) {
        this.userProfile = userProfile;
        this.categoryID = categoryID;
        this.score = score;
    }

    public TopTalent(UserProfile userProfile, String email, Integer statusUser, Sample sample, Exam exam, Integer categoryID, String date, Integer status, Integer score) {
        this.userProfile = userProfile;
        mEmail = email;
        mStatusUser = statusUser;
        mSample = sample;
        mExam = exam;
        this.categoryID = categoryID;
        this.date = date;
        this.status = status;
        this.score = score;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Sample getSample() {
        return mSample;
    }

    public void setSample(Sample sample) {
        mSample = sample;
    }

    public Exam getExam() {
        return mExam;
    }

    public void setExam(Exam exam) {
        mExam = exam;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public Integer getScore() {
        return score;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Integer getStatusUser() {
        return mStatusUser;
    }

    public void setStatusUser(Integer statusUser) {
        mStatusUser = statusUser;
    }
}
