package com.codingacademy.testerapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Ques implements Serializable {
    String ques;
    ArrayList<String> answers =new ArrayList<>();

    public Ques(String ques, ArrayList<String> answers) {
        this.ques = ques;
        this.answers = answers;
    }

    public String getQues() {
        return ques;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }
}
