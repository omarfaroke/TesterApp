package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;

public class Choice {
    @SerializedName("Choice_id")
    private Integer choiceId;

    @SerializedName("Choice_text")
    private String choiceText;

    @SerializedName("f_sample_id")
    private Integer quesSampleId;

    @SerializedName("answer")
    private int answer;

    public Choice(Integer choiceId, String choiceText, Integer quesSampleId, int answer) {
        this.choiceId = choiceId;
        this.choiceText = choiceText;
        this.quesSampleId = quesSampleId;
        this.answer = answer;
    }

    public Choice(String choiceText) {
        this.choiceText = choiceText;
    }

    public Integer getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Integer choiceId) {
        this.choiceId = choiceId;
    }

    public Choice() {
        this.choiceText = "aaaaaaaaaaaa";
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public Integer getQuesSampleId() {
        return quesSampleId;
    }

    public void setQuesSampleId(Integer quesSampleId) {
        this.quesSampleId = quesSampleId;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
