package com.example.tradeaaau.adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradeaaau.ClassFeatures.LessonActivity;
import com.example.tradeaaau.LessonPage;
import com.example.tradeaaau.R;

import com.example.tradeaaau.model.BookMarkModel;
import com.example.tradeaaau.model.QuizHistory;


import java.util.List;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.CourseViewHolder>{

    Context context;
    List<BookMarkModel> userNotes;

    public BookMarkAdapter(Context context, List<BookMarkModel> userNotes) {
        this.context = context;
        this.userNotes = userNotes;

    }

    @NonNull
    @Override
    public BookMarkAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookmark, parent, false);
        return new BookMarkAdapter.CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.bookmarkText.setText(userNotes.get(position).getNote());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, LessonActivity .class);
                intent.putExtra("courseId",userNotes.get(position).getCourseId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userNotes.size();
    }


    public static class CourseViewHolder extends RecyclerView.ViewHolder{


        TextView bookmarkText;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            bookmarkText = itemView.findViewById(R.id.tvNotesText);

        }
    }
}
