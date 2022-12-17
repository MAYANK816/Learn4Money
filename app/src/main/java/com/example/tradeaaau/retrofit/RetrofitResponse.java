package com.example.tradeaaau.retrofit;


import okhttp3.ResponseBody;
import retrofit2.Response;


public interface RetrofitResponse {
    public void onServiceResponse(int requestCode, Response<ResponseBody> response);
}
