package com.example.tradeaaau.model;

import com.google.gson.annotations.SerializedName;

public class BookMarkModel {
    @SerializedName("note")
    private String note;
    @SerializedName("course_id")
    private String CourseId;

    public BookMarkModel(String note, String courseId) {
        this.note = note;
        CourseId = courseId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }
}
