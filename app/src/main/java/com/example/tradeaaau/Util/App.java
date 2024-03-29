package com.example.tradeaaau.Util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

//import io.fabric.sdk.android.Fabric;


public class App extends Application
{
    public static String ENQUIRIES = null, QUOTATIONS = null;
    public static App mInstance;
    public static Context mcontext;
    public static String latitude = "", longitude = "";
    public static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate()
    {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        mInstance = this;
        mcontext = getApplicationContext();



        File directory = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            directory = new File(Environment.getExternalStorageDirectory() + File.separator + "TigerPay");
            if (!directory.exists())
                directory.mkdirs();
        }
        else {
            directory = getApplicationContext().getDir("TigerPay", Context.MODE_PRIVATE);
            if (!directory.exists())
                directory.mkdirs();
        }

        if (directory != null) {
            File books = new File(directory + File.separator + "Quotations");
            File video = new File(directory + File.separator + "Enquiries");

            if (!books.exists())
                books.mkdirs();

            if (!video.exists())
                video.mkdirs();

            QUOTATIONS = directory + File.separator + "Quotations";
            ENQUIRIES = directory + File.separator + "Enquiries";
        }
    }

    public static SharedPreferences getGlobalPefs()
    {
        return getContext().getSharedPreferences("Edulearn", 0);
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mcontext;
    }

    public static SharedPreferences getIdPref() {

         return getContext().getSharedPreferences("Edulearn", 0);
    }

    public static String getQUOTATIONS() {
        return QUOTATIONS;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}

