
package com.example.tradeaaau.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Course {

    @SerializedName("courseName")
    @Expose
    private String courseName;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("bestSeller")
    @Expose
    private String bestSeller;
    @SerializedName("member")
    @Expose
    private String member;
    @SerializedName("playlist")
    @Expose
    private List<Lesson> lesson = null;

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

    public String getBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(String bestSeller) {
        this.bestSeller = bestSeller;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public List<Lesson> getPlayList() {
        return lesson;
    }

    public void setPlayList(List<Lesson> lesson) {
        this.lesson = lesson;
    }

}
