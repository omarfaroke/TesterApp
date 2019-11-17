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

    public UserProfile(Integer userId, String fristName,  String lastName,String email, String password, String address, String phone) {
        UserId = userId;
        this.fristName = fristName;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }


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
