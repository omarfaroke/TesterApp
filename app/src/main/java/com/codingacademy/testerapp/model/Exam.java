package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Exam {
    public Exam(Integer examId, Integer categoryId, String examName, Date examDate, Integer examTime, Integer examPass, Integer status, List<Sample> samples) {
        this.examId = examId;
        this.categoryId = categoryId;
        this.examName = examName;
        this.examDate = examDate;
        this.examTime = examTime;
        this.examPass = examPass;
        this.status = status;
        this.samples = samples;
    }

    @SerializedName("exam_id")
    private Integer examId;

    @SerializedName("category_id")
    private Integer categoryId;

    @SerializedName("exam_name")
    private String examName;

    @SerializedName("exam_date")
    private Date examDate;

    @SerializedName("exam_time")
    private Integer examTime;

    @SerializedName("exam_pass")
    private Integer examPass;

    @SerializedName("exam_note")
    private Integer examNote;


    @SerializedName("exam_description")
    private Integer examDescription;

    private Integer status;

    List<Sample> samples;

    public Integer getExamId() {
        return examId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getExamName() {
        return examName;
    }

    public Date getExamDate() {
        return examDate;
    }

    public Integer getExamTime() {
        return examTime;
    }

    public Integer getExamPass() {
        return examPass;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getExamNote() {
        return examNote;
    }

    public Integer getExamDescription() {
        return examDescription;
    }

    public List<Sample> getSamples() {
        return samples;
    }


}
