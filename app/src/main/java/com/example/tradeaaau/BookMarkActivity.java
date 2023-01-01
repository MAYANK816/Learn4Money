package com.example.tradeaaau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.adapter.BookMarkAdapter;
import com.example.tradeaaau.adapter.UserHistoryAdapter;
import com.example.tradeaaau.model.BookMarkModel;
import com.example.tradeaaau.model.QuizHistory;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class BookMarkActivity extends AppCompatActivity implements RetrofitResponse {
    ArrayList<BookMarkModel> bookmarkList =new ArrayList<>();
    BookMarkAdapter bookMarkAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        recyclerView = findViewById(R.id.bookMarkListView);
        new UserRetrofitClient(this,BookMarkActivity.this,122,"wp-json/wp/v2/m_users/showBookmark?user_id="+ MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId)).callService(true);
    }
    public void BackFeature(View view) {
        finish();
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode){
            case 122:
                if(response.isSuccessful())
                {
                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        fetchData(result1.getJSONArray("data"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    private void fetchData(JSONArray data) {
        try {
            if(data.length()==0)
            {
                Constants.alertDialog(this,"No Notes Found");
            }
            else
            {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jb = new JSONObject(data.get(i).toString());
                    BookMarkModel bookMarkModel = new BookMarkModel(jb.getString("notes"),jb.getString("course_id"));
                    bookmarkList.add(bookMarkModel);
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                bookMarkAdapter = new BookMarkAdapter(this, bookmarkList);
                recyclerView.setAdapter(bookMarkAdapter);
                bookMarkAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


