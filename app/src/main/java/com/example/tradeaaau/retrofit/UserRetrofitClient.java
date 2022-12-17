package com.example.tradeaaau.retrofit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.tradeaaau.R;
import com.example.tradeaaau.Util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRetrofitClient {

    private ProgressDialog pd;
    private String url;
    private int requestCode;
    private RetrofitResponse result;
    private JsonObject postParam;
    private Call<ResponseBody> call;
    private Context mContext;
    private  static Retrofit retrofit;
    private static final String BASE_URL = "https://www.learn4money.com/";

    public UserRetrofitClient(Context mContext, RetrofitResponse result, int requestCode, String url)//for get request
    {
        this.mContext = mContext;
        this.result = result;
        this.requestCode = requestCode;
        this.url = url;
    }

    public UserRetrofitClient(Context mContext, RetrofitResponse result,
                              JsonObject postParam, int requestCode, String url)//for POST URL
    {

        this.mContext = mContext;
        this.result = result;
        this.postParam = postParam;
        this.requestCode = requestCode;
        this.url = url;
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .build();

            /*OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });*/
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void callService(boolean dialog) {
        try
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
            {
                pd = new ProgressDialog(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog));
            }
            else {
                pd = new ProgressDialog(mContext);
            }

            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);


            if (dialog)
            {
                pd.show();
            }

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()//Use For Time Out
                    .connectTimeout(4,TimeUnit.MINUTES)
                    .readTimeout(4,TimeUnit.MINUTES)
                    .build();

            System.setProperty("http.keepAlive", "false");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UserInterface retrofitService = retrofit.create(UserInterface.class);

            Log.e("url ", url);

            if (url.contains("login") || url.contains("courses") || url.contains("courseLessons") || url.contains("state")|| url.contains("getquiz")){
                Log.e("get service ", "service ");
                call = retrofitService.callGetService(url);
            }
            else{
                Log.e("POST register ", "service ");
                Log.e("url post ", url);
                Log.e("postParam ", postParam.toString());
                call = retrofitService.postRawJSON(url,postParam);
            }
            // for single image

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e("UserRetrofit",response.toString());
                        Log.e("call",call.toString());
                        if (response.isSuccessful())
                        {
                            Log.e("InsideRequestCode",requestCode+"");
                            result.onServiceResponse(requestCode, response);
                            pd.cancel();
                        }
                        else
                        {
                                Constants.alertDialog(mContext, "Error");
                        }
                        if (pd.isShowing()) {
                            pd.cancel();
                        }

                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    if (pd.isShowing()) {
                        pd.cancel();
                    }
                    call.cancel();
                    t.printStackTrace();
                    showAlertOnTimeOut("Connection Time Out", call, this);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void showAlertOnTimeOut(String message, final Call<ResponseBody> call, final Callback<ResponseBody> callbackk) {


        try {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new androidx.appcompat.view.ContextThemeWrapper(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog));
            alertDialogBuilder.setMessage(message);

            alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
//                if (!url.contains(Constant.MAKE_PAYMENT)) {
                    pd.show();
//                }
                    call.clone().enqueue(callbackk);
                }
            });


            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        }catch (Exception e){
        e.printStackTrace();
        }
    }



}
