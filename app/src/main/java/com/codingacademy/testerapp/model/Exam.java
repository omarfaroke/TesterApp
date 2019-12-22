package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
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

    @SerializedName("Question_number")
    private Integer questionNumber;

    @SerializedName("full_mark")
    private Integer fullMarks;

    @SerializedName("exam_pass")
    private Integer examPass;

    @SerializedName("exam_note")
    private String examNote;


    @SerializedName("exam_description")
    private String examDescription;

    @SerializedName("status")
    private Integer status;

    @SerializedName("examiner_id")
    private Integer examinerID;

    @SerializedName("examiner_name")
    private String examinerName;

    @SerializedName("sample")
   private Sample[] samples;

    public boolean expanded = false;

    public Exam(Integer examId, Integer categoryId, String examName, String examDate, Integer examTime, Integer questionNumber, Integer fullMarks, Integer examPass, String examNote, String examDescription, Integer status, Integer examinerID, Sample[] samples) {
        this.examId = examId;
        this.categoryId = categoryId;
        this.examName = examName;
        this.examDate = examDate;
        this.examTime = examTime;
        this.questionNumber = questionNumber;
        this.fullMarks = fullMarks;
        this.examPass = examPass;
        this.examNote = examNote;
        this.examDescription = examDescription;
        this.status = status;
        this.examinerID = examinerID;
        this.samples = samples;
    }

    public Exam(Integer examinerID) {
        this.examinerID = examinerID;
    }

    public Integer getExaminerID() {
        return examinerID;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public void setExamTime(Integer examTime) {
        this.examTime = examTime;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setFullMarks(Integer fullMarks) {
        this.fullMarks = fullMarks;
    }

    public void setExamPass(Integer examPass) {
        this.examPass = examPass;
    }

    public void setExamNote(String examNote) {
        this.examNote = examNote;
    }

    public void setExamDescription(String examDescription) {
        this.examDescription = examDescription;
    }

    public String getExaminerName() {
        return examinerName;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setSamples(Sample[] samples) {
        this.samples = samples;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

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

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public Integer getFullMarks() {
        return fullMarks;
    }

    public Integer getExamPass() {
        return examPass;
    }

    public String getExamNote() {
        return examNote;
    }

    public String getExamDescription() {
        return examDescription;
    }

    public Integer getStatus() {
        return status;
    }

    public Sample[] getSamples() {
        return samples;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
