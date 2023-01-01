package com.example.tradeaaau.model;

import com.google.gson.annotations.SerializedName;

public class QuizHistory {

    @SerializedName("id")
    private String id;

    @SerializedName("time")
    private String time;

    @SerializedName("user_ID")
    private String user_ID;

    @SerializedName("quiz_ID")
    private String quiz_ID;

    @SerializedName("quiz_result")
    private String quiz_result;

    @SerializedName("score_percent")
    private String score_percent;

    @SerializedName("course_id")
    private String course_id;

    @SerializedName("course_name")
    private String course_name;

    public QuizHistory(String id, String time, String user_ID, String quiz_ID, String quiz_result, String score_percent, String course_id, String course_name) {
        this.id = id;
        this.time = time;
        this.user_ID = user_ID;
        this.quiz_ID = quiz_ID;
        this.quiz_result = quiz_result;
        this.score_percent = score_percent;
        this.course_id = course_id;
        this.course_name = course_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore_percent() {
        return score_percent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourse_name() {
        return course_name;
    }


}
