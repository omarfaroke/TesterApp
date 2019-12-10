package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sample implements Serializable {
    @SerializedName("sample_id")
    private Integer sampleId;

    @SerializedName("sample_name")
    private String sampleName;

    private Integer status;

    @SerializedName("sample_date")
    String sample_date;

    @SerializedName("question")
    private Question[] questions;

    public boolean expanded;

    public Integer getSample_id() {
        return sampleId;
    }

    public Sample(Integer sampleId, String sampleName, Integer status, String sample_date) {
        this.sampleId = sampleId;
        this.sampleName = sampleName;
        this.status = status;
        this.sample_date = sample_date;
    }

    public Integer getSampleId() {
        return sampleId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public Integer getStatus() {
        return status;
    }

    public String getSample_date() {
        return sample_date;
    }

    public Question[] getQuestions() {
        return questions;
    }
}
