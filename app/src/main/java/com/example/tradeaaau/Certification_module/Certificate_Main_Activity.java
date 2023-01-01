package com.example.tradeaaau.Certification_module;

import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tradeaaau.R;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Certificate_Main_Activity extends AppCompatActivity {
    ArrayList<String> filenames;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_main);
        listView=findViewById(R.id.certificate_listview);
        Gson gson = new Gson();
        String json = MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_PdfFiles);
        Log.e("certificateActivity", json);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        filenames = gson.fromJson(json, type);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_view_bg,filenames);
        listView.setAdapter(adapter);
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