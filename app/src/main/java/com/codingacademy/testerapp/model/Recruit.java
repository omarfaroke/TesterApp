package com.codingacademy.testerapp.model;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Recruit implements Serializable {

    private Integer id ;

    @SerializedName("talent_id")
    private Integer talentId;

    @SerializedName("recruiter_id")
    private Integer recruiterId;


    private String date;


    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTalentId() {
        return talentId;
    }

    public void setTalentId(Integer talentId) {
        this.talentId = talentId;
    }

    public Integer getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Integer recruiterId) {
        this.recruiterId = recruiterId;
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
}
