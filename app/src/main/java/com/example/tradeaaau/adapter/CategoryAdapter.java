package com.example.tradeaaau.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tradeaaau.R;
import com.example.tradeaaau.LessonPage;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.model.Category;

import com.google.gson.Gson;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    List<Category> categoryList;
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }
//    public void setFilteredList(List<Category> filteredList)
//    {
//        this.categoryList=filteredList;
//        notifyDataSetChanged();
//    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_row_items, parent, false);
        // now here we create a recyclerview row items.
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {

        // here we will bind data in recyclerview ro items.

        holder.categoryName.setText(categoryList.get(position).getCategoryName());
//        holder.totalCategory.setText(categoryList.get(position).getTotalCourses());

        // for image we need to add glide image fetching library from netwok

        Glide.with(context).load(categoryList.get(position).getImage()).into(holder.categoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LessonPage.class);

                i.putExtra("courseId",categoryList.get(position).getCategoryId());
                i.putExtra("bgImage",categoryList.get(position).getImage());
                i.putExtra("courseName",categoryList.get(position).getCategoryName());
                Gson gson =new Gson();
                MySharedPrefs.getInstance(context.getApplicationContext()).putString("courseDetails",gson.toJson(categoryList.get(position)));
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        ImageView categoryImage;
        TextView categoryName, totalCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.course);
            categoryName = itemView.findViewById(R.id.course_name);
            totalCategory = itemView.findViewById(R.id.total_course);


        }
    }

}
