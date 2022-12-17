package com.example.tradeaaau;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tradeaaau.OTP.Sender;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.model.Category;
import com.example.tradeaaau.model.UserRegister;
import com.example.tradeaaau.retrofit.ApiInterface;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserInterface;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class registerActivity extends AppCompatActivity implements RetrofitResponse {
    EditText lgfname,lgemail,lglname,lgnumber;
    Spinner lgcity,lgcountry;
    Button subbtn;
    String city="";
    ArrayList<String> cityList=new ArrayList<>();
    String[] defaultState = {"Select state"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        initViews();
        subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDetails();
            }
        });
    }

    private void initViews() {
        lgfname = findViewById(R.id.lgfirstname);
        lgnumber=findViewById(R.id.llgnumber);
        lglname = findViewById(R.id.lglaststname);
        lgemail = findViewById(R.id.lgemail);
        lgcity = (Spinner) findViewById(R.id.lgcity);
        lgcountry = (Spinner) findViewById(R.id.lgcountry);
        subbtn=findViewById(R.id.lgbtn);
        new UserRetrofitClient(this,registerActivity.this,105,"wp-json/wp/v2/m_users/state?country_code=IN").callService(true);
        lgcity.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        city = String.valueOf(adapterView.getItemAtPosition(i));
                        MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_City,city);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
    }
    private void checkDetails() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Posting Data");
        progressDialog.show();

        if(TextUtils.isEmpty(lgemail.getText())){
            Toast.makeText(this,"Please fill Email details",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else if(TextUtils.isEmpty(lgfname.getText()) ){
            Toast.makeText(this,"Please fill First Name details",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else if(TextUtils.isEmpty(lglname.getText()) ){
            Toast.makeText(this,"Please fill Last Name details",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else if(TextUtils.isEmpty(lgnumber.getText()) ){
            Toast.makeText(this,"Please fill mobile details",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else
        {

            Log.e("userClick", "checkDetails: " );
            JsonObject postparams=new JsonObject();
//            postparams.addProperty("username",getIntent().getStringExtra("mobile"));
            postparams.addProperty("username",lgnumber.getText().toString());
            postparams.addProperty("email",lgemail.getText().toString());
            postparams.addProperty("first_name",lgfname.getText().toString());
            postparams.addProperty("last_name",lglname.getText().toString());
            postparams.addProperty("city",city);
            postparams.addProperty("country","India");
            Log.e("registerparams", postparams.toString());
            new UserRetrofitClient(this, registerActivity.this,postparams,102,"wp-json/wp/v2/m_users/register").callService(true);
            progressDialog.cancel();

        }
    }
    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        Log.e("register", "onPriorServiceResponse: "+response.toString() );
        switch (requestCode){
            case 102:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    Log.e("RegisterService", "onServiceResponse: "+result1);
                    if(response.isSuccessful())
                    {
                        if(result1.getString("message").equals("Username already exists"))
                        {
                            Constants.alertDialog(this,"User Already Exists");
                        }
                        else{
                            MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_Username,(lgfname.getText()+" "+lglname.getText()));
                            Random r = new Random();
                            int verification = (1000 + r.nextInt(9000));
                            Intent i=new Intent(registerActivity.this,OtpActivity.class);
                            Sender sender=new Sender("http://smsw.co.in/API/WebSMS/Http/v1.0a/index.php?username=learnm&password=V3mYTr-eHAy&sender=LNGLRN&to=",lgnumber.getText().toString(),"&message=Your OTP to log in to your account is "+String.valueOf(verification)+" Do not share your OTP with anyone. LNGLRN&reqid=1&format="+"{json|text}&pe_id=1701166842155123411&template_id=1707166910539917807");
                            sender.call();
                            i.putExtra("mobile",lgnumber.getText().toString());
                            i.putExtra("otp",String.valueOf(verification));
                            startActivity(i);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 105:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    if(response.isSuccessful()) {
                         for(int i=0;i<result1.length();i++)
                            {
                          cityList.add(result1.getString(String.valueOf(result1.names().get(i))));
                         }
                        lgcity.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,cityList));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }
    public void LoginScreen(View view) {
        Intent i=new Intent(registerActivity.this,LoginActivity.class);
        startActivity(i);
    }
}