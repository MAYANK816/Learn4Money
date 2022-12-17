package com.example.tradeaaau.ClassFeatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.tradeaaau.MainActivity;
import com.example.tradeaaau.R;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.adapter.VideoLessonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LessonActivity extends AppCompatActivity implements VideoLessonAdapter.UserVideoVIew, MediaPlayer.OnCompletionListener, VideoLessonAdapter.BookMarkInterface {
    RecyclerView courseRecyclerView;
    VideoLessonAdapter videoLessonAdapter;
    int currvideo = 0;
    VideoView videoView;
    int position=0;
    JSONArray videoData;
    ProgressDialog progressDialog;
    List<String> lessonPlaylist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        initViews();
    }
    private void initViews() {

        position= Integer.parseInt(getIntent().getStringExtra("idx"));
        courseRecyclerView = findViewById(R.id.main_course_recycler);
        progressDialog =new ProgressDialog(this);
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching progress");
        progressDialog.show();
        String VideoJsonArray =getIntent().getStringExtra("lessonVideos");
        Log.e("LessonActivity", String.valueOf(VideoJsonArray) );
        try {
            videoData=new JSONArray(VideoJsonArray);
            getCourseContent(videoData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void playVideo(String id) {
        progressDialog.show();
        videoView = findViewById(R.id.lessonsvideoView);
        Log.e("LessonActivity", "playVideo: "+id );
        Uri uri = Uri.parse(id);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(id);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                progressDialog.cancel();
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(this::onCompletion);
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
        AlertDialog.Builder obj = new AlertDialog.Builder(this);
        obj.setTitle("Playback Finished!");
        obj.setIcon(R.mipmap.ic_launcher);
        LessonActivity.MyListener m = new LessonActivity.MyListener();
        obj.setPositiveButton("Replay", m);
        obj.setNegativeButton("Next", m);
        obj.setMessage("Want to replay or play next video?");
        obj.show();
    }

    public void HomeFeature(View view) {
        Intent i=new Intent(LessonActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void SaveBookmark(String lessonName) {
        MySharedPrefs.getInstance(getApplicationContext()).addTask(Constants.L4M_Favourite,"Lesson Name: "+getIntent().getStringExtra("LessonName")+"\n"+"Notes: "+lessonName);
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

}