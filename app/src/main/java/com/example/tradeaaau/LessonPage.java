package com.example.tradeaaau;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LessonPage extends AppCompatActivity implements  RetrofitResponse, LessonAdapter.ProgressInterace {
    RecyclerView courseRecyclerView;
    LessonAdapter lessonAdapter;
    TextView CourseName,progressText;
    String id="";
    String image="";
    List<Lesson> lessonList=new ArrayList<>();
    ImageView bgImage;
    ProgressDialog progressDialog;
    LinearProgressIndicator progressBar;
    static int countProgress, lessonSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);
        initViews();
        new UserRetrofitClient(this, LessonPage.this,104,"wp-json/wp/v2/m_users/courseLessons?course_id="+id).callService(true);
        progressDialog.cancel();
    }

    // will add one mor argument for checkbox
    private void getCourseContent(JSONArray lessonarr, JSONArray video){
        try{
            lessonSize=lessonarr.length();
            for (int i = 0; i<lessonSize; i++) {
                JSONObject jb=new JSONObject(lessonarr.get(i).toString());
                Log.e("getLessonPage", jb.toString());
                lessonList.add(new Lesson(jb.getString("ID"),jb.getString("post_title"),"5.35 mins",false));
                //have to remove after api fix
                /*
                 * HashMap will store the id's of lesson that are already had been watched by user
                 *
                 * if(map.containsKey(jb.getString("ID"))
                 * {
                 * then make checkbox true
                 * */
            }

//            Log.e("getLessonPageProgress", String.valueOf((2*100)/4));
//            progressText.setText("Your Progress: "+(2*100)/4+"%");
//            progressBar.setProgress((2*100)/4);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            courseRecyclerView.setLayoutManager(layoutManager);
            lessonAdapter = new LessonAdapter(this, lessonList,video,this);
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
        CourseName=findViewById(R.id.name);
        progressText=findViewById(R.id.progressTextBox);
        countProgress=0;
        lessonSize=0;
        progressBar=findViewById(R.id.lessonProgressBar);
        progressText.setText("Your Progress: "+"0%");
        bgImage=findViewById(R.id.imageView6);
        CourseName.setText(getIntent().getStringExtra("courseName"));
        image=getIntent().getStringExtra("bgImage");
        Glide.with(getApplicationContext()).load(image).into(bgImage);
        id=getIntent().getStringExtra("courseId");
        courseRecyclerView = findViewById(R.id.lesson_recycler);
        progressDialog =new ProgressDialog(this);
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching progress");
        progressDialog.show();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        Log.e("LessonPage", "onPriorServiceResponse: " + response.toString());
        switch (requestCode){
            case 104:
                JSONObject result1 = null;
                try {
                    result1 = new JSONObject(response.body().string());
                    getCourseContent(result1.getJSONArray("lessons"),result1.getJSONArray("videos"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
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

    @Override
    public void SaveProgress(String LessonId) {
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
}
