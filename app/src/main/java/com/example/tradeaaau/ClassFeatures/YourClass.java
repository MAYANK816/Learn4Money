package com.example.tradeaaau.ClassFeatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tradeaaau.LessonPage;
import com.example.tradeaaau.MainActivity;
import com.example.tradeaaau.R;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.model.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class YourClass extends AppCompatActivity {
    Category categoryList;
    TextView tx1,tx2,tx3;
    CardView cardView;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_class);
        tx1=findViewById(R.id.txcategoryId);
        tx2=findViewById(R.id.txcategorycategoryName);
        tx3=findViewById(R.id.txcategorytotalCourses);
        cardView=findViewById(R.id.cvItemClass);
        img=findViewById(R.id.courseImage);
        Gson gson = new Gson();
        String json = MySharedPrefs.getInstance(getApplicationContext()).getString("courseDetails");
        Type type = new TypeToken<Category>() {}.getType();
        categoryList = gson.fromJson(json, type);
            tx1.setText(categoryList.getCategoryId());
            tx2.setText(categoryList.getCategoryName());
            tx3.setText(categoryList.getTotalCourses());
            Glide.with(getApplicationContext()).load(categoryList.getImage()).into(img);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), LessonPage.class);
                    i.putExtra("courseId",categoryList.getCategoryId());
                    startActivity(i);
                }
            });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void BackFeature(View view) {
        finish();
    }
    public void HomeFeature(View view) {
        Intent i=new Intent(YourClass.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}