package com.example.tradeaaau.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tradeaaau.R;
import com.example.tradeaaau.model.Category;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderHolder>{

    private Context context;
    List<String> images;

    public SliderAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public SliderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_row_item, parent, false);
        return new SliderAdapter.SliderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderHolder holder, int position) {
        Glide.with(context).load(images.get(position)).into(holder.sliderImage);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class SliderHolder extends RecyclerView.ViewHolder{

        ImageView sliderImage;

        public SliderHolder(@NonNull View itemView) {
            super(itemView);

            sliderImage = itemView.findViewById(R.id.DashboardSlider);



        }
    }
}
