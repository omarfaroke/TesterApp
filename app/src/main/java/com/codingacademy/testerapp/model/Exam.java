package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Exam implements Serializable {
    @SerializedName("exam_id")
    private Integer examId;

    @SerializedName("category_id")
    private Integer categoryId;

    @SerializedName("exam_name")
    private String examName;

    @SerializedName("exam_date")
    private String examDate;

    @SerializedName("exam_time")
    private Integer examTime;

    @SerializedName("exam_pass")
    private Integer examPass;

    @SerializedName("exam_note")
    private String examNote;


    @SerializedName("exam_description")
    private String examDescription;

    @SerializedName("status")
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

    public String getExamDate() {
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

    public String getExamNote() {
        return examNote;
    }

    public String getExamDescription() {
        return examDescription;
    }

    public List<Sample> getSamples() {
        return samples;
    }

    public Exam(Integer examId, Integer categoryId, String examName, String examDate, Integer examTime, Integer examPass, String examNote, String examDescription, Integer status) {
        this.examId = examId;
        this.categoryId = categoryId;
        this.examName = examName;
        this.examDate = examDate;
        this.examTime = examTime;
        this.examPass = examPass;
        this.examNote = examNote;
        this.examDescription = examDescription;
        this.status = status;
    }
}
