package com.example.tradeaaau;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tradeaaau.ClassFeatures.YourClass;
import com.example.tradeaaau.OTP.Sender;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MyProfile extends AppCompatActivity implements RetrofitResponse {
    TextView email, mobile, country;
    EditText Name, State;
    TextView Submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initViews();
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateMyProfile();

            }
        });
    }

    private void UpdateMyProfile() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Posting Data");
        progressDialog.show();
        if (TextUtils.isEmpty(Name.getText())) {
            Toast.makeText(this, "Please fill Name details", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (TextUtils.isEmpty(State.getText())) {
            Toast.makeText(this, "Please fill State details", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            Log.e("MyProfile", "onClick: ");
            JsonObject postparams = new JsonObject();
            postparams.addProperty("username", mobile.getText().toString());
            postparams.addProperty("email", email.getText().toString());
            postparams.addProperty("first_name", Name.getText().toString());
            postparams.addProperty("last_name", "");
            postparams.addProperty("city", State.getText().toString());
            postparams.addProperty("country", "India");
            Log.e("profileUpdate", postparams.toString());
            new UserRetrofitClient(this, MyProfile.this, postparams, 106, "wp-json/wp/v2/m_users/profile?user_id=" + MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId)).callService(true);
            progressDialog.cancel();
        }
    }

    private void initViews() {

        Name = findViewById(R.id.txPfName);
        State = findViewById(R.id.txPfState);
        email = findViewById(R.id.txPfEmail);
        mobile = findViewById(R.id.txPfMobile);
        country = findViewById(R.id.txPfCountry);
        Name.setText(MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_Username));
        State.setText(MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_City));
        email.setText(MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_Email));
        mobile.setText(MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_Mobilenumber));
        Submit = findViewById(R.id.txPfSubmit);
//        country.setText(MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.Country));
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        Log.e("profileUpdation", "onPriorServiceResponse: " + response.toString());
        switch (requestCode) {
            case 106:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    Log.e("RegisterService", "onServiceResponse: " + result1);
                    if (response.isSuccessful() && result1.getString("data").equals("User updated successfully")) {
                        MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_Username,Name.getText().toString());
                        MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_Email,email.getText().toString() );
                        MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_City, State.getText().toString() );
                        MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_Mobilenumber,  mobile.getText().toString());
                        Constants.alertDialog(this, "User updated successfully");
                    }
                    else{
                        Constants.alertDialog(this, result1.getString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void BackFeature(View view) {
        finish();
    }
    public void HomeFeature(View view) {
        Intent i=new Intent(MyProfile.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}