package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    @SerializedName("Ques_id")
    private Integer quesId;

    @SerializedName("Ques_text")
    private String quesText;

    @SerializedName("status")
    private Integer quesStatus;

    @SerializedName("score")
    private Integer quesScore;

    @SerializedName("f_sample_id")
    private Integer quesSampleId;

    @SerializedName("choices")
    private List<Choice> choices;

    public Question(String quesText, List<Choice> choices) {
        this.quesText = quesText;
        this.choices = choices;
    }

    public Question(Integer quesId, String quesText, Integer quesStatus, Integer quesScore, Integer quesSampleId, List<Choice> choices) {
        this.quesId = quesId;
        this.quesText = quesText;
        this.quesStatus = quesStatus;
        this.quesScore = quesScore;
        this.quesSampleId = quesSampleId;
        this.choices = choices;
    }

    public Integer getQuesId() {
        return quesId;
    }

    public void setQuesId(Integer quesId) {
        this.quesId = quesId;
    }

    public String getQuesText() {
        return quesText;
    }

    public void setQuesText(String quesText) {
        this.quesText = quesText;
    }

    public Integer getQuesStatus() {
        return quesStatus;
    }

    public void setQuesStatus(Integer quesStatus) {
        this.quesStatus = quesStatus;
    }

    public Integer getQuesScore() {
        return quesScore;
    }

    public void setQuesScore(Integer quesScore) {
        this.quesScore = quesScore;
    }

    public Integer getQuesSampleId() {
        return quesSampleId;
    }

    public void setQuesSampleId(Integer quesSampleId) {
        this.quesSampleId = quesSampleId;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }


}
