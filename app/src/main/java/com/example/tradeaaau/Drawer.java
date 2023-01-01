package com.example.tradeaaau;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tradeaaau.Certification_module.Certificate_Main_Activity;
import com.example.tradeaaau.ClassFeatures.ClassUpdate;
import com.example.tradeaaau.ClassFeatures.LiveClass;
import com.example.tradeaaau.ClassFeatures.Subscriptions;
import com.example.tradeaaau.ClassFeatures.YourClass;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.model.Category;
import com.google.android.material.navigation.NavigationView;


public class Drawer extends AppCompatActivity implements View.OnClickListener
{
    TextView txName,tvName;
    public View view;
    public FrameLayout frame;
    public static DrawerLayout drawer;
    public Toolbar toolbar;
    public NavigationView navigationView;
    public ActionBarDrawerToggle drawerToggle;
    ImageView ivCross,imgProfile,ivIvg;
    Intent intent;
    Category categoryData;
    LinearLayout llDashboard,llQuick,llquiz,lllogout,llHistory,llLogin,llclasUpdate,llAppShare,llContact,llLiveClass,llCertificates,llSubscription,llBookmark;

    @Override
    public void setContentView(int layoutResID) {

        view = getLayoutInflater().inflate(R.layout.activity_drawer, null);
        frame = (FrameLayout) view.findViewById(R.id.frame);
        getLayoutInflater().inflate(layoutResID, frame, true);

        super.setContentView(view);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

//        navigationView.inflateMenu(R.menu.drawer_menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txName = (TextView) findViewById(R.id.txName);
        tvName = (TextView) findViewById(R.id.tvName);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);

        setSupportActionBar(toolbar);



        toolbar.setTitle("");
        toolbar.setSubtitle("");
        tvName.setText(MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_Username));

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        InitializeView();



      //  tvViewProfile.setOnClickListener(this);

    }

    private void InitializeView() {

        llDashboard = (LinearLayout) findViewById(R.id.yourclass);
        lllogout=(LinearLayout) findViewById(R.id.lllogout);
        llCertificates=findViewById(R.id.llCertificates);
        llquiz=findViewById(R.id.Quizclass);
        llBookmark=findViewById(R.id.llBookmark);
        llQuick = (LinearLayout) findViewById(R.id.llLiveClass);
        ivCross = (ImageView) findViewById(R.id.ivCross);
        llLiveClass=findViewById(R.id.llLiveClass);
        llContact=findViewById(R.id.llContact);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        llLogin=findViewById(R.id.llLogin);
        llSubscription=findViewById(R.id.llSubscription);
        llAppShare=findViewById(R.id.llShareApp);
        llclasUpdate=findViewById(R.id.llClassUpdate);
        llLogin.setOnClickListener(this);
        llHistory=findViewById(R.id.llHistory);
        llDashboard.setOnClickListener(this);
        llLiveClass.setOnClickListener(this);
        llHistory.setOnClickListener(this);
        llAppShare.setOnClickListener(this);
        llContact.setOnClickListener(this);
        llSubscription.setOnClickListener(this);
        llclasUpdate.setOnClickListener(this);
        lllogout.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        llBookmark.setOnClickListener(this);
        llquiz.setOnClickListener(this);
        llQuick.setOnClickListener(this);
        ivCross.setOnClickListener(this);
        llCertificates.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        drawer.closeDrawers();

        switch (v.getId())
        {

            case R.id.ivCross:
                drawer.closeDrawers();
                break;
            case R.id.Quizclass:
                intent = new Intent(Drawer.this, QuizActivity.class);
                startActivity(intent);
                break;

            case R.id.lllogout:
//                PreferenceFile.getInstance().saveData(Drawer.this,Constant.ID,null);
                MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_AccountStatus,null);
                MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_Username,null);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
            case R.id.llBookmark:

                    intent = new Intent(Drawer.this, BookMarkActivity.class);
                    startActivity(intent);
                break;

            case R.id.yourclass:
                String json = MySharedPrefs.getInstance(getApplicationContext()).getString("courseDetails");
                if(json!=null)
                {
                    intent = new Intent(Drawer.this, YourClass.class);
                    startActivity(intent);
                }
                else{
                    Constants.alertDialog(this,"You Haven't Visit Any Class");
                }
                break;
            case R.id.llHistory:
                intent=new Intent(Drawer.this,HistoryActivity.class);
                startActivity(intent);
                break;

            case R.id.llContact:
                intent = new Intent(Drawer.this,newSupportEmail.class);
                startActivity(intent);
                break;
            case R.id.llLogin:
                intent = new Intent(Drawer.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.llClassUpdate:
                intent = new Intent(Drawer.this, ClassUpdate.class);
                startActivity(intent);
                break;
            case R.id.llShareApp:
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Choose an app to ");
                String sharingmessage="https://play.google.com/store/app/details?id="+BuildConfig.APPLICATION_ID;
                sendIntent.putExtra(Intent.EXTRA_TEXT,sharingmessage);
                startActivity(Intent.createChooser(sendIntent, "Share By"));
                break;
            case R.id.llLiveClass:
                intent=new Intent(Drawer.this, LiveClass.class);
                startActivity(intent);
                break;
            case R.id.llCertificates:
                String json2 = MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_PdfFiles);
                if(json2!=null)
                {
                    intent = new Intent(Drawer.this, Certificate_Main_Activity.class);
                    startActivity(intent);
                }
                else{
                    Constants.alertDialog(this,"You Haven't Got Any Certificate");
                }

                break;
            case R.id.llSubscription:
                intent = new Intent(Drawer.this, Subscriptions.class);
                startActivity(intent);
                break;
            case R.id.imgProfile:
                intent=new Intent(Drawer.this,MyProfile.class);
                startActivity(intent);
                break;
        }

    }


}
