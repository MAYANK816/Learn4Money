package com.example.tradeaaau.ClassFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.tradeaaau.LoginActivity;
import com.example.tradeaaau.R;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.registerActivity;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class LiveClass extends AppCompatActivity implements RetrofitResponse {
    String Zoom_Id="",Zoom_Pass="",CourseId="";
    TextView txId,txPass,txVenue;
    LottieAnimationView comingSoonImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class);
        initView();
        callAPI();
    }

    private void initView() {
        txId=findViewById(R.id.txmeetingId);
        txPass=findViewById(R.id.txmeetingpasscode);
        txVenue=findViewById(R.id.txmeetingDate);
        comingSoonImg=(LottieAnimationView ) findViewById(R.id.imgComingSoon) ;
        comingSoonImg.setAnimation(R.raw.cominsoon);
    }

    private void callAPI() {
        JsonObject postparams=new JsonObject();
        postparams.addProperty("ID", MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId));
        new UserRetrofitClient(this, LiveClass.this,postparams,107,"wp-json/wp/v2/m_users/zoom").callService(true);
    }

    public void openZoomMethod(View view) {

        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("us.zoom.videomeetings");
        if (intent != null) {
            // change zoom link here
            Uri uri=Uri.parse("https://us05web.zoom.us/j/"+Zoom_Id+"?pwd="+Zoom_Pass);
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }
        else{
            Toast.makeText(getApplicationContext(),"You haven't installed Zoom",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode){
            case 107:
                Log.e("LiveClassResponse", response.toString());
                if(response.isSuccessful() && response.code()==200)
                {
                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        Zoom_Id=result1.getString("zoom_id");
                        Zoom_Pass=result1.getString("zoom_password");
                        txId.setText(Zoom_Id);
                        txPass.setText(Zoom_Pass);
                        txVenue.setText(result1.getString("date_&_time"));
                        CourseId=result1.getString("course_category");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }
    public void BackFeature(View view) {
        finish();
    }
}