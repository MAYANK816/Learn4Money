package com.example.tradeaaau;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.tradeaaau.ClassFeatures.YourClass;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.adapter.CategoryAdapter;
import com.example.tradeaaau.adapter.SliderAdapter;
import com.example.tradeaaau.model.Category;
import com.example.tradeaaau.retrofit.RetrofitResponse;
import com.example.tradeaaau.retrofit.UserRetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends Drawer implements View.OnClickListener, RetrofitResponse {
    RecyclerView categoryRecyclerView;
    ImageSlider imageSlider;
    CategoryAdapter categoryAdapter;
    //    SearchView searchView;
    ProgressDialog progressDialog;
    List<Category> categoryList=new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        if (MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_UserId) == null) {
            progressDialog.setTitle("Fetching User Details");
            new UserRetrofitClient(this, MainActivity.this, 101, "wp-json/wp/v2/m_users/login/?username=" + MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_Mobilenumber)).callService(true);
            progressDialog.cancel();
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

               switch ( item.getItemId()){

                   case R.id.nav_BookMark:
                       String json = MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_Favourite);
                       if(json==null)
                       {
                           Constants.alertDialog(MainActivity.this,"No BookMarks are there");
                       }
                       else
                    startActivity(new Intent(getApplicationContext(), BookMarkActivity.class));
                    break;

                   case R.id.nav_profile:

                       startActivity(new Intent(getApplicationContext(), MyProfile.class));
                       break;
               }

                return true;
            }
        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchFilterfun(newText);
//                return true;
//            }
//        });
    }

    private void initViews() {
//        searchView.clearFocus();
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        categoryRecyclerView = findViewById(R.id.course_recycler);
        imageSlider = findViewById(R.id.sliderrecycler);
        progressDialog = new ProgressDialog(this);
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching progress");
        progressDialog.show();
        new UserRetrofitClient(this, MainActivity.this,103,"wp-json/wp/v2/m_users/courses").callService(true);
        progressDialog.cancel();

    }

//    private void searchFilterfun(String newText) {
//        List<Category> filterList=new ArrayList<>();
//        for(Category category: categoryList){
//            if(category.getCategoryName().toLowerCase().contains(newText.toLowerCase()))
//            {
//                filterList.add(category);
//            }
//        }
//        if(filterList.isEmpty())
//        {
//            Toast.makeText(getApplicationContext(),"No data Found",Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            categoryAdapter.setFilteredList(filterList);
//        }
//    }

    private void getAllCategory(JSONArray categoryList2, JSONArray image) {
     try{
         for (int i = 0; i<categoryList2.length(); i++) {
            JSONObject jb=new JSONObject(categoryList2.get(i).toString());
             if(!(jb.getString("post_status").equals("publish"))){
                 i++;
             }
             else{
                 String course_Id = jb.getString("ID");
                 String course_name = jb.getString("post_title");
                 String totalCourses = String.valueOf(jb.length());
                 String course_img = String.valueOf(image.get(i));
                 categoryList.add(new Category(course_Id,course_name,totalCourses,course_img));
             }
         }
         RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
         categoryRecyclerView.setLayoutManager(layoutManager);
         categoryAdapter = new CategoryAdapter(this, categoryList);
         categoryRecyclerView.setAdapter(categoryAdapter);
         categoryAdapter.notifyDataSetChanged();

     } catch (JSONException e) {
         Log.e("getAllCategory", e.getMessage());
         e.printStackTrace();
     }

    }

    private void setSlider(JSONArray arr) {
        List<SlideModel> sliderImages = new ArrayList<>();
        try {

            for (int i=0;i<arr.length();i++) {
                sliderImages.add(new SlideModel(String.valueOf(arr.get(i)),ScaleTypes.FIT));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        SliderRecyclerView.setLayoutManager(layoutManager);
//        sliderAdapter = new SliderAdapter(this, sliderImages);
//        SliderRecyclerView.setAdapter(sliderAdapter);
//        sliderAdapter.notifyDataSetChanged();
        imageSlider.setImageList(sliderImages,ScaleTypes.FIT);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        Log.e("login", "onPriorServiceResponse: " + response.toString());
        switch (requestCode) {
            case 101:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    Log.e("login", "onServiceResponse: " + result1.toString());
                    if (response.isSuccessful() && response.code() == 200) {
                        Log.e("login", "onServiceResponse: " + result1.getJSONObject("data").getString("user_nicename") + " " + result1.getJSONObject("data").getString("user_email"));
                        MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_UserId, result1.getJSONObject("data").getString("ID"));
                    }

                    MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_AccountStatus, "True");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case 103:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    getAllCategory(result1.getJSONArray("courses"),result1.getJSONArray("image"));
                    setSlider(result1.getJSONArray("image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void YourClassView(View view) {
        String json = MySharedPrefs.getInstance(getApplicationContext()).getString("courseDetails");
        if(json==null)
        {
            Constants.alertDialog(this,"You Haven't Visit Any Class");
        }
        else
        startActivity(new Intent(this,YourClass.class));
    }
}