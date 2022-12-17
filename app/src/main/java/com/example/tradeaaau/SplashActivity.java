package com.example.tradeaaau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;

public class SplashActivity extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
        public void run() {
            if(MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_AccountStatus)==null) {
                i = new Intent(SplashActivity.this, LoginActivity.class);
            }
            else {
                i = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(i);
            finish();
        }
    }, 2000);
    }
}