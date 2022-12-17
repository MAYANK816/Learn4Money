package com.example.tradeaaau;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tradeaaau.OTP.Sender;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserInterface;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements RetrofitResponse {
    Button loginBtn;
    EditText lgnumber;
    CountryCodePicker cpp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login );
        loginBtn=findViewById(R.id.lgsgbtn);
        lgnumber=findViewById(R.id.lgnumber);
        cpp=findViewById(R.id.ccpPicker);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.hideKeyboard(LoginActivity.this,view);
                checkDetails();
            }
        });
    }
    private void checkDetails() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Verifying Details");
        progressDialog.show();
        if (TextUtils.isEmpty(lgnumber.getText()) )
        {
            Toast.makeText(this,"Please fill Password details",Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
        }
        else
        {
            new UserRetrofitClient(this, LoginActivity.this,101,"wp-json/wp/v2/m_users/login/?username="+lgnumber.getText().toString()).callService(true);
            progressDialog.cancel();
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        Log.e("login", "onPriorServiceResponse: "+response.toString() );
        switch (requestCode){
            case 101:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    Log.e("login", "onServiceResponse: "+result1.has("message"));
                    if(response.isSuccessful()) {
                        if (result1.has("message")) {
                            Constants.alertWithIntent(this,"User not registered",registerActivity.class);

                        }else {
                            Log.e("login", "onServiceResponse: " + result1.getJSONObject("data").getString("user_nicename") + " " + result1.getJSONObject("data").getString("user_email"));
                            Random r = new Random();
                            int verification = (1000 + r.nextInt(9000));
                            Intent i = new Intent(LoginActivity.this, OtpActivity.class);
                            Sender sender = new Sender("http://smsw.co.in/API/WebSMS/Http/v1.0a/index.php?username=learnm&password=V3mYTr-eHAy&sender=LNGLRN&to=", lgnumber.getText().toString(), "&message=Your OTP to log in to your account is " + String.valueOf(verification) + " Do not share your OTP with anyone. LNGLRN&reqid=1&format=" + "{json|text}&pe_id=1701166842155123411&template_id=1707166910539917807");
                            sender.call();
                            MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_UserId, result1.getJSONObject("data").getString("ID"));
                            MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_AccountStatus, "True");
                            MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_Username, result1.getJSONObject("data").getJSONObject("extra").getString("first_name") + " " + result1.getJSONObject("data").getJSONObject("extra").getString("last_name"));
                            MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_Email, result1.getJSONObject("data").getString("user_email"));
                            MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_City, result1.getJSONObject("data").getJSONObject("extra").getString("user_city"));
                            MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_Mobilenumber, result1.getJSONObject("data").getString("user_login"));
                            i.putExtra("mobile", lgnumber.getText().toString());
                            i.putExtra("otp", String.valueOf(verification));
                            startActivity(i);
                            }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void RegisterScreen(View view) {
        Intent i=new Intent(LoginActivity.this,registerActivity.class);
        startActivity(i);
    }

}