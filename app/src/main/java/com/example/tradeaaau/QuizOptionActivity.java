package com.example.tradeaaau;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class QuizOptionActivity extends AppCompatActivity implements RetrofitResponse {
 RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_option);
        init();
//        CardView cvStock = findViewById(R.id.cvMath);
//        CardView cvForex = findViewById(R.id.cvGeography);

        findViewById(R.id.imageViewQuizOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    new UserRetrofitClient(this,QuizOptionActivity.this,108,"https://www.learn4money.com/wp-json/wp/v2/m_users/getquiz?course_id="+getIntent().getStringExtra("CourseID")).callService(true);

//
//        cvStock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(QuizOptionActivity.this, StockQuizActivity.class);
//                intent.putExtra(Constants.SUBJECT,getString(R.string.math));
//                startActivity(intent);
//            }
//        });

    }
    public void init(){
        recyclerView=findViewById(R.id.quizrecyclerView);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        
        Log.e("LessonPage", "onPriorServiceResponse: " + response.toString());
        switch (requestCode){
            case 104:
                JSONObject result1 = null;
                try {
                    result1 = new JSONObject(response.body().string());
                    setQuizView(result1.getJSONArray("questions"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setQuizView(JSONArray questions) {
    }
}