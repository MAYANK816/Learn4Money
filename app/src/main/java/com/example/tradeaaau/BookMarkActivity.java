package com.example.tradeaaau;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class BookMarkActivity extends AppCompatActivity {
    ArrayList<String> bookmarkList =new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        listView=findViewById(R.id.bookMarkListView);
        Gson gson = new Gson();
        String json = MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_Favourite);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            Log.e("type", type.toString());
            Log.e("BookMarkList", gson.fromJson(json, type).toString());
            bookmarkList = gson.fromJson(json, type);
            ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_view_bg,bookmarkList);
            listView.setAdapter(adapter);
    }
    public void BackFeature(View view) {
        finish();
    }
}


