package com.example.tradeaaau.ClassFeatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.tradeaaau.MainActivity;
import com.example.tradeaaau.R;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.adapter.VideoLessonAdapter;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LessonActivity extends AppCompatActivity implements VideoLessonAdapter.UserVideoVIew, MediaPlayer.OnCompletionListener, VideoLessonAdapter.BookMarkInterface, RetrofitResponse {
    RecyclerView courseRecyclerView;
    VideoLessonAdapter videoLessonAdapter;
    int currvideo = 0;
    VideoView videoView;
    int position=0;
    JSONArray videoData;
    ProgressDialog progressDialog;
    Button masBtn;
    List<String> lessonPlaylist=new ArrayList<>();
    View fullScreenView;
    static int videoPlayed=0;
    static boolean flag=false;
    String id="";
    Boolean isChecked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        initViews();
        masBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            saveLessonProgress();
            }
        });
    }

    private void saveLessonProgress() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId));
        jsonObject.addProperty("lesson_id", getIntent().getStringExtra("LessonId"));
        new UserRetrofitClient(this, LessonActivity.this, jsonObject, 123, "wp-json/wp/v2/m_users/finishLesson").callService(true);
    }

    public void saveInstanceVideo(){
        MySharedPrefs.getInstance(getApplicationContext()).putString("VideoWatched", String.valueOf(videoView.getCurrentPosition()));
        fullScreenView.setVisibility(View.VISIBLE);
        if(flag){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            flag=false;
        }
        else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            flag=true;
        }
        videoView.start();
    }
    private void initViews() {
        fullScreenView=findViewById(R.id.ffview);
        isChecked=getIntent().getBooleanExtra("isChecked",false);
        masBtn=findViewById(R.id.markAsBtn);
        videoView = findViewById(R.id.lessonsvideoView);
        courseRecyclerView = findViewById(R.id.main_course_recycler);
        progressDialog =new ProgressDialog(this);
        progressDialog.create();
        progressDialog.setCancelable(false);
        if(isChecked){
            masBtn.setVisibility(View.GONE);
        }
        if(!(MySharedPrefs.getInstance(getApplicationContext()).getString("VideoWatched")==null)) {
            videoView.seekTo(Integer.parseInt(MySharedPrefs.getInstance(getApplicationContext()).getString("VideoWatched")));
        }
        String VideoJsonArray = getIntent().getStringExtra("lessonVideos");
        try {
            if(VideoJsonArray!=null) {
                position= Integer.parseInt(getIntent().getStringExtra("idx"));
                videoData = new JSONArray(VideoJsonArray);
                getCourseContent(videoData);
            }
            else{
                id=getIntent().getStringExtra("courseId");
                new UserRetrofitClient(LessonActivity.this, LessonActivity.this,104,"wp-json/wp/v2/m_users/courseLessons?course_id="+id).callService(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void playVideo(String id) {
        try {
            progressDialog.setMessage("Loading Video ");
            progressDialog.show();
            Uri uri = Uri.parse(id.trim());
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            MediaController mediaController = new MediaController(this);
            mediaController.setMediaPlayer(videoView);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            Log.e("LessonActivity", "playVideo: "+uri.toString());
            progressDialog.show();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    progressDialog.cancel();
                    videoView.start();
                    fullScreenView.setVisibility(View.VISIBLE);
                }
            });
            fullScreenView.findViewById(R.id.fullscreenimg).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveInstanceVideo();
                }
            });
            videoView.setOnCompletionListener(this::onCompletion);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void getCourseContent(JSONArray lesson){
        try {
            JSONObject jsonObject=new JSONObject(lesson.get(position+1).toString());
           JSONArray jsonArray=jsonObject.getJSONArray("lesson-video");
           for(int i=0;i<jsonArray.length();i++)
            lessonPlaylist.add(String.valueOf(jsonArray.get(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        courseRecyclerView.setLayoutManager(layoutManager);
        videoLessonAdapter = new VideoLessonAdapter(this, lessonPlaylist,this,this);
        courseRecyclerView.setAdapter(videoLessonAdapter);
        videoLessonAdapter.notifyDataSetChanged();
        progressDialog.cancel();
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        fullScreenView.setVisibility(View.GONE);
        AlertDialog.Builder obj = new AlertDialog.Builder(this);
        obj.setTitle("Playback Finished!");
        obj.setIcon(R.mipmap.ic_launcher);
        LessonActivity.MyListener m = new LessonActivity.MyListener();
        obj.setPositiveButton("Replay", m);
        obj.setMessage("Want to replay ?");
        obj.show();
    }

    public void HomeFeature(View view) {
        Intent i=new Intent(LessonActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void SaveBookmark(String lessonName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId));
        jsonObject.addProperty("course_id", getIntent().getStringExtra("course_id"));
        jsonObject.addProperty("lesson_id", getIntent().getStringExtra("LessonId"));
        jsonObject.addProperty("notes","Lesson Name: " + getIntent().getStringExtra("LessonName") + "\nNote: " + lessonName);
        new UserRetrofitClient(this, LessonActivity.this, jsonObject, 121, "wp-json/wp/v2/m_users/saveBookmark").callService(true);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode){
            case 121:
                if(response.isSuccessful())
                {
                    new SweetAlertDialog(LessonActivity.this)
                            .setTitleText("BookMark has been saved successfully")
                            .show();
                }
                break;
            case 123:
                if(response.isSuccessful())
                {
                    new SweetAlertDialog(LessonActivity.this)
                            .setTitleText("Lesson Marked as Completed!")
                            .show();
                }
                break;
            case 104:
                if(response.isSuccessful())
                {
                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        getCourseContent(result1.getJSONArray("videos"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    class MyListener implements DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which)
        {
            if (which == -1) {
                videoView.seekTo(0);
                videoView.start();
            }
            else {
                ++currvideo;
//                if (currvideo == videolist.size())
//                    currvideo = 0;
//                setVideo(videolist.get(currvideo));
            }
        }
    }
    public void BackFeature(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismiss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

}