package com.example.tradeaaau;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;

import com.example.tradeaaau.adapter.UserHistoryAdapter;

import com.example.tradeaaau.model.QuizHistory;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity implements RetrofitResponse {
     RecyclerView recyclerView;
     List<QuizHistory> userHistory= new ArrayList<>();
     UserHistoryAdapter userHistoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.rvHistoryRecyclerView);
        new UserRetrofitClient(this,HistoryActivity.this,120,"wp-json/wp/v2/m_users/showQuizResult?user_id="+ MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId)).callService(true);
        findViewById(R.id.imageViewHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode){
            case 120:
             if(response.isSuccessful())
             {
                 try {
                     JSONObject result1 = new JSONObject(response.body().string());
                     fetchData(result1.getJSONArray("data"));
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

             }
                break;
        }
    }

    private void fetchData(JSONArray data) {
        try {
        for(int i=0;i<data.length();i++)
        {
                JSONObject jb=new JSONObject(data.get(i).toString());
                QuizHistory quizHistory=new QuizHistory(jb.getString("id"), jb.getString("time"),jb.getString("user_ID"),jb.getString("quiz_ID"),jb.getString("quiz_result"),jb.getString("score_percent"),jb.getString("course_id"),jb.getString("course_name"));
                userHistory.add(quizHistory);
        }
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            userHistoryAdapter = new UserHistoryAdapter(this, userHistory);
            recyclerView.setAdapter(userHistoryAdapter);
            userHistoryAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}