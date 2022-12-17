package com.example.tradeaaau;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.example.tradeaaau.OTP.Sender;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;


import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.TimeZone;

public class OtpActivity extends AppCompatActivity {
TextView txno,chronometer;
    CountDownTimer countDownTimer;
    EditText edfirst, edSecond, edthird, edForth;
    int time = 4 * 60 * 1000; //20 seconds
    int interval = 1000; // 1 second
    TextView txNext, txback, txResend;
    String enterotp="",otp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otp);
        txno=findViewById(R.id.txno);
        txno.setText(getIntent().getStringExtra("mobile"));
        otp = getIntent().getStringExtra("otp");
        edfirst = (EditText) findViewById(R.id.edfirst);
        edSecond = (EditText) findViewById(R.id.edSecond);
        edthird = (EditText) findViewById(R.id.edthird);
        edForth = (EditText) findViewById(R.id.edForth);
        txNext = (TextView) findViewById(R.id.txNext);
        txback = (TextView) findViewById(R.id.txback);
        txResend = (TextView) findViewById(R.id.txResend);
        chronometer = (TextView) findViewById(R.id.chronometer);
        callTimer();
        txNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callmethod();
            }
        });
        edfirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edfirst.getText().toString().length() == 1)     //size as per your requirement
                {
                    edSecond.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edSecond.getText().toString().length() == 1)     //size as per your requirement
                {
                    edthird.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edthird.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edthird.getText().toString().length() == 1)     //size as per your requirement
                {
                    edForth.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edfirst.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                   hideKeyboard(OtpActivity.this, v);
                    callmethod();
                }
                return false;
            }
        });

        edSecond.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    hideKeyboard(OtpActivity.this, v);
                    callmethod();
                }
                return false;
            }
        });

        edthird.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    hideKeyboard(OtpActivity.this, v);
                    callmethod();
                }
                return false;
            }
        });

        edForth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    hideKeyboard(OtpActivity.this, v);
                    callmethod();
                }
                return false;
            }
        });

        txResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                int verification = (1000 + r.nextInt(9000));
                Sender sender=new Sender("http://smsw.co.in/API/WebSMS/Http/v1.0a/index.php?username=learnm&password=V3mYTr-eHAy&sender=LNGLRN&to=",getIntent().getStringExtra("mobile"),"&message=Your OTP to log in to your account is "+String.valueOf(verification)+" Do not share your OTP with anyone. LNGLRN&reqid=1&format={json|text}&pe_id=1701166842155123411&template_id=1707166910539917807");
                sender.call();
                otp= String.valueOf(verification);
                callTimer();
                txResend.setVisibility(View.GONE);
                txNext.setVisibility(View.VISIBLE);
            }
        });
    }
    public void callTimer() {

        countDownTimer = new CountDownTimer(time, interval) {
            public void onTick(long millisUntilFinished) {
                chronometer.setText(getDateFromMillis(millisUntilFinished));
            }

            public void onFinish() {
                txResend.setVisibility(View.VISIBLE);
                enterotp = "";
                edfirst.setText("");
                edSecond.setText("");
                edthird.setText("");
                edForth.setText("");

                chronometer.setText("No seconds left!");
                otp = "";
                txNext.setVisibility(View.GONE);
            }
        };
        countDownTimer.start();
    }
    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        df.setTimeZone(TimeZone.getDefault().getTimeZone("GMT"));
        return df.format(d);
    }
    void callmethod() {
        enterotp = edfirst.getText().toString() + edSecond.getText().toString() + edthird.getText().toString() + edForth.getText().toString();
        Log.e("enteredotp", "Otp: "+enterotp );
        if (enterotp.equals(otp)) {
            countDownTimer.cancel();
            try {


                    final Dialog dialog = new Dialog(OtpActivity.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.constant_dialog);
                    int width = WindowManager.LayoutParams.MATCH_PARENT;
                    int height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setLayout(width, height);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    params.gravity = Gravity.CENTER;
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    TextView tvText = (TextView) dialog.findViewById(R.id.tvText);
                    TextView btnok = (TextView) dialog.findViewById(R.id.btnok);
                    tvText.setText("User Verified Successfully");

                    btnok.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_AccountStatus,"True");
                            Intent i= new Intent(OtpActivity.this, MainActivity.class);
                            startActivity(i);
                            finishAffinity();
                            dialog.dismiss();
                        }
                    });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}