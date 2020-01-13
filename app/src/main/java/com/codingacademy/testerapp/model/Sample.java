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

    public Sample(Integer sampleId, String sampleName, Integer status, String sample_date, Integer f_exam_id, Question[] questions) {
        this.sampleId = sampleId;
        this.sampleName = sampleName;
        this.status = status;
        this.sample_date = sample_date;
        this.f_exam_id = f_exam_id;
        this.questions = questions;
    }

    @SerializedName("f_exam_id")
    private Integer f_exam_id;

    @SerializedName("question")
    private Question[] questions;

    public transient boolean expanded;


    public Integer getSample_id() {
        return sampleId;
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

    public void setSampleId(Integer sampleId) {
        this.sampleId = sampleId;
    }
}
