package com.example.tradeaaau.adapter;


import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tradeaaau.ClassFeatures.LessonActivity;
import com.example.tradeaaau.R;

import com.example.tradeaaau.model.Lesson;

import org.json.JSONArray;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.CourseViewHolder> {

    Context context;
    List<Lesson> lessons;
    JSONArray video;
//    ProgressInterace progressInterace;
    String id;
    public LessonAdapter(Context context, List<Lesson> lessons, JSONArray video,String id) {
        this.context = context;
        this.lessons = lessons;
        this.video=video;
        this.id=id;

    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_list_row_items, parent, false);

        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        int i=position+1;

        holder.contentNumber.setText("0"+i);

        holder.contentTime.setText(lessons.get(position).getTime());

        if(lessons.get(position).getChecked()==true) {
            Glide.with(context).load(R.drawable.check).into(holder.progressBox);
        }

        if(lessons.get(position).getName().length()>30) {
            holder.contentName.setText(lessons.get(position).getName().substring(0, 30) + ".....");
        }
        else {
            holder.contentName.setText(lessons.get(position).getName());
        }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, LessonActivity.class);
                i.putExtra("idx",String.valueOf(position));
                i.putExtra("course_id",id);
                i.putExtra("isChecked",lessons.get(position).getChecked());
                i.putExtra("LessonName",lessons.get(position).getName());
                i.putExtra("LessonId",lessons.get(position).getID());
                i.putExtra("lessonVideos", String.valueOf(video));
                context.startActivity(i);
            }
        });

    }
    @Override
    public int getItemCount() {
        return lessons.size();
    }


    public static class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView contentNumber, contentTime, contentName;
        ImageView playbutton,progressBox;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            contentName = itemView.findViewById(R.id.content_title);
            contentTime = itemView.findViewById(R.id.lesson_content_time);
            contentNumber = itemView.findViewById(R.id.content_number);
            playbutton=itemView.findViewById(R.id.imageView3);
            progressBox=itemView.findViewById(R.id.lessonCheckbox);

        }
    }
//    public interface ProgressInterace{
//        void SaveProgress(String id);
//    }

}
