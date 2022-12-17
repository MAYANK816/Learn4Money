package com.example.tradeaaau.Util;

/**
 * Created by pro55 on 28/4/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MySharedPrefs {
    private static MySharedPrefs sSharedPrefs;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    List<String> list;
    Gson gson=new Gson();
    private MySharedPrefs(Context context) {
        mPref = context.getSharedPreferences("Edulearn", Context.MODE_PRIVATE);
    }


    public static MySharedPrefs getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new MySharedPrefs(context.getApplicationContext());
        }
        return sSharedPrefs;
    }
    public void putString(String key, String val) {
        mEditor = mPref.edit();
        mEditor.putString(key, val);
        mEditor.commit();
    }
//    public void putStringList(String key, List<String> val) {
//        mEditor = mPref.edit();
//        mEditor.putStringSet(key, val);
//        mEditor.commit();
//    }
    public void addTask(String key,String value) {
        if (null == list) {
            list = new ArrayList<String>();
        }
        list.add(value);

        // save the task list to preference
        mEditor = mPref.edit();
        mEditor.putString(key, gson.toJson(list));
        mEditor.commit();
    }

    public String getString(String key) {
        return mPref.getString(key, null);
    }
}