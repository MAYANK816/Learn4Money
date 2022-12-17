package com.example.tradeaaau;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tradeaaau.other.Constants;
import com.example.tradeaaau.other.Utils;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity implements RetrofitResponse {

    private int currentQuestionIndex = 0;
    private TextView tvQuestion, tvQuestionNumber;
    private Button btnNext;
    private RadioGroup radioGroup;
    private List<String> questions;
    private int correctQuestion = 0;
    private Map<String, Map<String, Boolean>> questionsAnswerMap;
    private int QuizSize=0;
    private String QuizID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geography_or_literature_quiz);
        new UserRetrofitClient(this, QuizActivity.this,108,"wp-json/wp/v2/m_users/getquiz?course_id="+getIntent().getStringExtra("CourseID")).callService(true);
        initViews();
        findViewById(R.id.btnNextQuestionLiteratureAndGeography).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton =  findViewById(radioGroup.getCheckedRadioButtonId());
                boolean answer = questionsAnswerMap.get(questions.get(currentQuestionIndex)).get(radioButton.getText());

                if (answer){
                    correctQuestion++;
                }
                currentQuestionIndex++;
                if (btnNext.getText().equals(getString(R.string.next))){
                     displayNextQuestions();
                }else{
                    Intent intentResult = new Intent(QuizActivity.this,FinalResultActivity.class);
                    intentResult.putExtra(Constants.SUBJECT,getIntent().getStringExtra("subject"));
                    intentResult.putExtra(Constants.CORRECT,correctQuestion);
                    intentResult.putExtra(Constants.INCORRECT,QuizSize - correctQuestion);
                    intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentResult);
                    finish();
                }

            }
        });
        findViewById(R.id.imageViewStartQuizGeographyOrLiterature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void displayNextQuestions() {
        setAnswersToRadioButton();
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText("Current Question: " + (currentQuestionIndex + 1));

        if (currentQuestionIndex == QuizSize - 1){
            btnNext.setText(getText(R.string.finish));
        }

    }

    private void displayData() {
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText("Current Question: " + (currentQuestionIndex + 1));
        setAnswersToRadioButton();
    }

    private void setAnswersToRadioButton(){

        ArrayList<String> questionKey = new ArrayList(questionsAnswerMap.get(questions.get(currentQuestionIndex)).keySet());

        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.removeAllViews();
        for(int i=0;i<questionKey.size();i++)
        {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(questionKey.get(i));
            radioGroup.addView(radioButton);
        }


    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        Log.e("LessonPage", "onPriorServiceResponse: " + response.toString());
        switch (requestCode){
            case 108:
                JSONArray result1 = null;
                try {
                    result1 = new JSONArray(response.body().string());
                    JSONObject jsonObject=new JSONObject(result1.get(0).toString());
                    setQuizView(jsonObject.getJSONArray("questions"));
                   displayData();
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
        questionsAnswerMap = Utils.getRandomQuizQuestions(this,getIntent().getStringExtra("subject"),QuizSize,questionsData);
        questions = new ArrayList<>(questionsAnswerMap.keySet());
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.textView78);
        tvQuestionNumber = findViewById(R.id.textView18);
        btnNext = findViewById(R.id.btnNextQuestionLiteratureAndGeography);
        radioGroup = findViewById(R.id.radioGroup);

    }

}