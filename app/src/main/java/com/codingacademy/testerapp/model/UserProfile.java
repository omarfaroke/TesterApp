package com.codingacademy.testerapp.model;

public class UserProfile {


    private Integer UserId;

    private String fristName;

    private String email;

    private String password;


    private String lastName;

    private String address;

    private String phone;

    private String imageUrl;

    private int type;

    private int status;


    public Integer getUserId() {
        return UserId;
    }

    public String getFristName() {
        return fristName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }
}
