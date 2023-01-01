package com.example.tradeaaau;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tradeaaau.Util.Constants;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.other.Utils;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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
    int pageWidth = 1650 ;
    int pageHeight = 1275 ;
    Bitmap imageBitmap, scaledImageBitmap,imageBitmap2,scaledImageBitmap2;
    boolean radioSelected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geography_or_literature_quiz);
        new UserRetrofitClient(this, QuizActivity.this,108,"wp-json/wp/v2/m_users/getquiz?course_id="+getIntent().getStringExtra("CourseID")).callService(true);
        imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.certificate1);
        imageBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.certificate2);
        scaledImageBitmap = Bitmap.createScaledBitmap(imageBitmap, 1700, 400, false);
        scaledImageBitmap2=Bitmap.createScaledBitmap(imageBitmap2, 1700, 300, false);
        initViews();

        findViewById(R.id.btnNextQuestionLiteratureAndGeography).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioSelected)
                {
                    RadioButton radioButton =  findViewById(radioGroup.getCheckedRadioButtonId());
                    boolean answer = questionsAnswerMap.get(questions.get(currentQuestionIndex)).get(radioButton.getText());

                    Log.e("Quiz", "onClick: "+answer);
                    if (answer){
                        correctQuestion++;
                    }
                    currentQuestionIndex++;
                }

                if (btnNext.getText().equals(getString(R.string.next))){
                     displayNextQuestions();
                }else{
                   callSaveAPI();
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

    private void callSaveAPI() {
        DateTimeFormatter dtf = null;
        JsonObject jsonObject=new JsonObject();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            jsonObject.addProperty("time",dtf.format(now));
        }
        jsonObject.addProperty(Constants.L4M_user_id, MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId));
        jsonObject.addProperty(Constants.L4M_Quizid, MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_QuizId));
        if(((correctQuestion*100)/QuizSize )>=60)
         jsonObject.addProperty("quiz_result","Pass");
        else
         jsonObject.addProperty("quiz_result","Fail");
         jsonObject.addProperty("score_percent",((correctQuestion*100)/QuizSize )+"%");
         jsonObject.addProperty("course_id",getIntent().getStringExtra("CourseID"));
         jsonObject.addProperty("course_name",getIntent().getStringExtra("subject"));
//        jsonObject.addProperty("course_id",13454);
        new UserRetrofitClient(this,QuizActivity.this,jsonObject,109,"wp-json/wp/v2/m_users/getQuizResult").callService(true);

    }

    private void displayNextQuestions() {
        radioSelected=false;
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
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioSelected=true;
                }
            });
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
                    if(result1.length()==0)
                    {
                        finish();
                    }
                    JSONObject jsonObject=new JSONObject(result1.get(0).toString());
                    MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_QuizId,new JSONObject(jsonObject.getString("ID")).getString("quiz_Id") );
                    setQuizView(jsonObject.getJSONArray("questions"));
                    displayData();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 109:
                if(response.isSuccessful())
                {
                    if(((correctQuestion*100)/QuizSize )>=60)
                    createPDF();
                    else{
                        Constants.alertDialog(this, "You have scored less than 60%");
                    }
                }

                break;
        }
    }

    private void createPDF() {
        String  name=  MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_Username).trim();
        String courseName=getIntent().getStringExtra("subject");
        String marks=((correctQuestion*100)/QuizSize )+"%";
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint newpaint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(scaledImageBitmap, 0, 0, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(100);
        newpaint.setTextSize(40);
        newpaint.setTextAlign(Paint.Align.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            paint.setTypeface(Typeface.create(ResourcesCompat.getFont(getApplicationContext(),R.font.greatvibesregular), Typeface.BOLD_ITALIC));
            newpaint.setTypeface(Typeface.create(ResourcesCompat.getFont(getApplicationContext(),R.font.nunito_sans_semibold), Typeface.ITALIC));
        }
        canvas.drawText(name, pageWidth / 2, 600, paint);
        canvas.drawText(" This is to certify that "+name+" has been successfully completed ", pageWidth/1.3f, 800, newpaint);
        canvas.drawText("the "+courseName+" course with "+marks+"marks.", pageWidth /1.5f, 850, newpaint);
        canvas.drawBitmap(scaledImageBitmap2, 0,1050, paint);
        pdfDocument.finishPage(page);
        String pdfPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/PDF_CERTIFICATE"+System.currentTimeMillis();
        File file =new File(pdfPath);
        if(!file.exists())
        {
            file.mkdirs();
        }
        File pdf_file=new File(file.getPath()+".pdf");

        if(!pdf_file.exists()) {
            try {
                pdf_file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MySharedPrefs.getInstance(QuizActivity.this).addTaskCertificate(Constants.L4M_PdfFiles,"Certificate Location: "+pdf_file.getPath());
        try {
            pdfDocument.writeTo(new FileOutputStream(pdf_file));

        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
        Constants.alertWithIntent(this, "Score Saved and Certificate Generated Successfully",MainActivity.class);

    }    private void setQuizView(JSONArray questionsData) {

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