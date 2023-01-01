package com.example.tradeaaau;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.adapter.LessonAdapter;
import com.example.tradeaaau.model.Lesson;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LessonPage extends AppCompatActivity implements  RetrofitResponse, Runnable {
    RecyclerView courseRecyclerView;
    LessonAdapter lessonAdapter;
    TextView CourseName,progressText;
    String id="",image="";
    List<Lesson> lessonList;
    ImageView bgImage;
    LinearProgressIndicator progressBar;
    boolean alreadyRead=false;
    static int countProgress, lessonSize;
    HashMap<String, Boolean> lessonRead;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);
        initViews();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAPIS();
                swipeContainer.setRefreshing(false);
            }
        });

    }

    private void fetchAPIS() {
        lessonList.clear();
        countProgress=0;
        lessonSize=0;
        new Handler(Looper.getMainLooper()).post(() -> {
            new UserRetrofitClient(LessonPage.this, LessonPage.this,124,"wp-json/wp/v2/m_users/finishLessonIds?user_id="+MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId)).callService(true);
        });
        new Handler(Looper.getMainLooper()).post(() -> {
            new UserRetrofitClient(LessonPage.this, LessonPage.this,104,"wp-json/wp/v2/m_users/courseLessons?course_id="+id).callService(true);
        });
    }

    private void getCourseContent(JSONArray lessonarr, JSONArray video){
        try{
            lessonSize=lessonarr.length();
            for (int i = 0; i<lessonSize; i++) {
                JSONObject jb=new JSONObject(lessonarr.get(i).toString());
                Log.e("getLessonPage", jb.toString());
                if(lessonRead.containsKey(jb.getString("ID"))){
                    SaveProgress();
                    alreadyRead=true;
                }
                else{
                    alreadyRead=false;
                }
                lessonList.add(new Lesson(jb.getString("ID"),jb.getString("post_title"),"5.35 mins",alreadyRead));
            }
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            courseRecyclerView.setLayoutManager(layoutManager);
            lessonAdapter = new LessonAdapter(this, lessonList,video,id);
            courseRecyclerView.setAdapter(lessonAdapter);
            lessonAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.e("getAllCategory", e.getMessage());
            e.printStackTrace();
        }

    }

    public void BackFeature(View view) {
        finish();
    }

    private void initViews() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        lessonRead=new HashMap<>();
        CourseName=findViewById(R.id.name);
        progressText=findViewById(R.id.progressTextBox);
        countProgress=0;
        lessonSize=0;
        lessonList=new ArrayList<>();
        progressBar=findViewById(R.id.lessonProgressBar);
        progressText.setText("Your Progress: "+"0%");
        bgImage=findViewById(R.id.imageView6);
        CourseName.setText(getIntent().getStringExtra("courseName"));
        image=getIntent().getStringExtra("bgImage");
        Glide.with(getApplicationContext()).load(image).into(bgImage);
        id=getIntent().getStringExtra("courseId");
        courseRecyclerView = findViewById(R.id.lesson_recycler);
//        fetchAPIS();
        new Handler(Looper.getMainLooper()).post(() -> {
            new UserRetrofitClient(LessonPage.this, LessonPage.this,124,"wp-json/wp/v2/m_users/finishLessonIds?user_id="+MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId)).callService(true);
        });
        new Handler(Looper.getMainLooper()).post(() -> {
            new UserRetrofitClient(LessonPage.this, LessonPage.this,104,"wp-json/wp/v2/m_users/courseLessons?course_id="+id).callService(true);
        });
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode){
            case 104:
                if(response.isSuccessful())
                {
                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        getCourseContent(result1.getJSONArray("lessons"),result1.getJSONArray("videos"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 124:
                if(response.isSuccessful())
                {
                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        if(!(result1.getString("data").equals("No lesson found")))
                            getAllLessons(result1.getJSONArray("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void getAllLessons(JSONArray data) {
        try{
            for (int i = 0; i<data.length(); i++) {
                lessonRead.put(data.get(i).toString(),true);
            }

        } catch (Exception e) {
            Log.e("LessonPage", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void HomeFeature(View view) {
        Intent i=new Intent(LessonPage.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void SaveProgress() {
        ++countProgress;
        Log.e("getLessonPageProgress", String.valueOf((countProgress*100)/lessonSize));
            progressText.setText("Your Progress: "+(countProgress*100)/lessonSize+"%");
            progressBar.setProgress((countProgress*100)/lessonSize);
        if(progressBar.getProgress()==100)
        {
        TextView QuizTxt=findViewById(R.id.quiztextv);
        QuizTxt.setVisibility(View.VISIBLE);
        }
    }

    public void AttemptQuiz(View view) {
        Intent intent = new Intent(LessonPage.this, QuizActivity.class);
        intent.putExtra("CourseID",id);
        intent.putExtra("subject",getIntent().getStringExtra("courseName"));
        startActivity(intent);
    }

    @Override
    public void run() {

    }
}
