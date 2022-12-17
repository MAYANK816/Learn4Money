package com.example.tradeaaau.other;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.example.tradeaaau.model.QuestionPair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Utils {

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String formatDate(long time){
        SimpleDateFormat formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

    public static Map<String,Map<String,Boolean>> getQuestions(JSONArray questionsData){
        HashMap<String,Map<String,Boolean>> questions = new HashMap<>();
        try {
            for (int i = 0; i < questionsData.length(); i++) {
                JSONObject jb = new JSONObject(questionsData.get(i).toString());

                JSONArray jsonArray=new JSONArray(jb.getJSONArray("answers").toString());

                HashMap<String,Boolean> answer1 = new HashMap<>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    if(Integer.parseInt(jb.getString("correctAnswer"))==j) {
                        answer1.put(String.valueOf(jsonArray.get(j)), true);
                    }
                    else {
                        answer1.put(String.valueOf(jsonArray.get(j)), false);
                    }
                }
                questions.put(jb.getString("question"),answer1);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public static Map<String,Map<String,Boolean>> getRandomQuizQuestions(Context context, String subject, int SIZE, JSONArray questionsData){

        Map<String,Map<String, Boolean>> originalQuestion;
        originalQuestion = getQuestions(questionsData);

        return originalQuestion;
    }

}
