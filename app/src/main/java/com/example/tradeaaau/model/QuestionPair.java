package com.example.tradeaaau.model;

import org.json.JSONArray;

public class QuestionPair {
    String rightanswer;
    JSONArray answers;

    public QuestionPair(String rightanswer, JSONArray answers) {
        this.rightanswer = rightanswer;
        this.answers = answers;
    }

    public String getRightanswer() {
        return rightanswer;
    }

    public void setRightanswer(String rightanswer) {
        this.rightanswer = rightanswer;
    }

    public JSONArray getAnswers() {
        return answers;
    }

    public void setAnswers(JSONArray answers) {
        this.answers = answers;
    }


}
