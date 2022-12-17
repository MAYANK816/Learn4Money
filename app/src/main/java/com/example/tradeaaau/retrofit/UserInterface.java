package com.example.tradeaaau.retrofit;


import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UserInterface {

    @POST
    Call<ResponseBody> postRawJSON(@Url String url, @Body JsonObject jsonObject);

    @GET
    Call<ResponseBody> callGetService(@Url String url);


}
