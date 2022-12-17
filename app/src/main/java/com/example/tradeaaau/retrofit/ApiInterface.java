package com.example.tradeaaau.retrofit;


import com.example.tradeaaau.model.Category;
import com.example.tradeaaau.model.Course;
import com.example.tradeaaau.model.Course_new;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("category")
    Call<List<Category>> getAllCategory();

    @GET("course")
    Call<List<Course>> getCourseContent();

    // we need to make model class for our data
    // first have a look on json structure.

}
