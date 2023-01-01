package com.example.tradeaaau.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tradeaaau.R;

import com.example.tradeaaau.model.QuizHistory;


import java.util.List;

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.CourseViewHolder>{

    Context context;
    List<QuizHistory> userHistory;

    public UserHistoryAdapter(Context context, List<QuizHistory> userHistory) {
        this.context = context;
        this.userHistory = userHistory;

    }

    @NonNull
    @Override
    public UserHistoryAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new UserHistoryAdapter.CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
    holder.contentName.setText(userHistory.get(position).getCourse_name());
    holder.contentNumber.setText(userHistory.get(position).getScore_percent()+"%");
    holder.contentTime.setText(userHistory.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return userHistory.size();
    }


    public static class CourseViewHolder extends RecyclerView.ViewHolder{


        TextView contentNumber, contentTime, contentName;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            contentName = itemView.findViewById(R.id.tvcoursename);
            contentTime = itemView.findViewById(R.id.tvcoursetime);
            contentNumber = itemView.findViewById(R.id.tvcoursescore);

        }
    }
}
