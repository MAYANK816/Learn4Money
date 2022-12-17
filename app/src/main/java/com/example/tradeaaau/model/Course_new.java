package com.example.tradeaaau.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course_new {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("courseName")
    @Expose
    private String courseName;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("member")
    @Expose
    private String member;

    public Course_new(String id, String courseName, String rating, String price, String member) {
        this.id = id;
        this.courseName = courseName;
        this.rating = rating;
        this.price = price;
        this.member = member;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}
