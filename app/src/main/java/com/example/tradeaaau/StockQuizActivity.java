package com.example.tradeaaau;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tradeaaau.model.QuestionPair;
import com.example.tradeaaau.other.Constants;
import com.example.tradeaaau.other.Utils;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class StockQuizActivity extends AppCompatActivity implements RetrofitResponse {

    private int currentQuestionIndex = 0;
    private TextView tvQuestion, tvQuestionNumber;
    private Button btnNext;
    private List<String> questions;
    private int correctQuestion = 0;
    private EditText etAnswer;
    private HashMap<String, QuestionPair> questionsAnswerMap;
    private int QuizSize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz);

        new UserRetrofitClient(this,StockQuizActivity.this,108,"https://www.learn4money.com/wp-json/wp/v2/m_users/getquiz?course_id="+getIntent().getStringExtra("CourseID")).callService(true);
        initViews();

        findViewById(R.id.imageViewStartQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer = etAnswer.getText().toString();

                if (answer.isEmpty()){
                    Toast.makeText(StockQuizActivity.this, "Answer cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (questionsAnswerMap.get(questions.get(currentQuestionIndex)).equals(answer)) {
                    correctQuestion++;
                }
                currentQuestionIndex++;

                if (btnNext.getText().equals(getString(R.string.next))){
                    displayNextQuestions();
                }else{
                    Intent intentResult = new Intent(StockQuizActivity.this,FinalResultActivity.class);
                    intentResult.putExtra(Constants.SUBJECT,getString(R.string.math));
                    intentResult.putExtra(Constants.CORRECT,correctQuestion);
                    intentResult.putExtra(Constants.INCORRECT,Constants.QUESTION_SHOWING - correctQuestion);
                    intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentResult);
                    finish();
                }
            }
        });

        displayData();
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.textView8);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumberMath);
        btnNext = findViewById(R.id.btnNextQuestionMath);
        etAnswer = findViewById(R.id.tietEnterAnswerMath);

    }

    private void displayNextQuestions() {
        etAnswer.setText("");
        tvQuestion.setText(questions.get(currentQuestionIndex) + " = ?");
        tvQuestionNumber.setText("Current Question: " + (currentQuestionIndex + 1));

        if (currentQuestionIndex == QuizSize  - 1){
            btnNext.setText(getText(R.string.finish));
        }
    }

    private void displayData() {
        tvQuestion.setText(questions.get(currentQuestionIndex) + " = ?");
        tvQuestionNumber.setText("Current Question: " + (currentQuestionIndex + 1));
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
    private void setQuizView(JSONArray questionsData) {
        QuizSize=questionsData.length();
        try {
            for (int i = 0; i < QuizSize; i++) {
                JSONObject jb = new JSONObject(questionsData.get(i).toString());
                questionsAnswerMap.put(jb.getString("question"),new QuestionPair(jb.getString("correctAnswer"),jb.getJSONArray("answers")));
            }
        }catch (JSONException e) {
                e.printStackTrace();
            }

        questions = new ArrayList<>(questionsAnswerMap.keySet());
    }
}