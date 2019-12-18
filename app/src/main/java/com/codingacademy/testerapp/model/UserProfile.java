package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("f_user_id")
    private Integer UserId;

    private Integer type;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("m_name")
    private String middleName;

    @SerializedName("last_name")
    private String lastName;

    private String address;

    private String phone;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("email")
    private String email;

    @SerializedName("status")
    private int status;

    public transient boolean expanded=false;



    public UserProfile(Integer userId, Integer type, String firstName, String middleName, String lastName, String address, String phone, String imageUrl) {
        UserId = userId;
        this.type = type;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
