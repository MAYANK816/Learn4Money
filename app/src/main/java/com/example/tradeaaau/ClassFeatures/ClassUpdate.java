package com.example.tradeaaau.ClassFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tradeaaau.R;

public class ClassUpdate extends AppCompatActivity {
    LottieAnimationView comingSoonImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_update);
        initView();
    }
    private void initView() {
        comingSoonImg=(LottieAnimationView ) findViewById(R.id.imgComingSoon) ;
        comingSoonImg.setAnimation(R.raw.cominsoon);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void BackFeature(View view) {
        finish();
    }
}