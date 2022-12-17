package com.example.tradeaaau.Certification_module;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;


import com.example.tradeaaau.R;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.model.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Certificate_Main_Activity extends AppCompatActivity {
    List<String> filenames;
    Button btnCreate;
    public final int REQUEST_CODE = 100;
    int pageWidth = 1650 ;
    int pageHeight = 1275 ;
    Bitmap imageBitmap, scaledImageBitmap,imageBitmap2,scaledImageBitmap2;
    EditText ed1,ed2,ed3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_certificate_main);
        imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.certificate1);
        imageBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.certificate2);
        Gson gson = new Gson();
        String json = MySharedPrefs.getInstance(getApplicationContext()).getString(Constants.L4M_PdfFiles);
        Type type = new TypeToken<Category>() {}.getType();
        filenames=(json!=null?gson.fromJson(json, type):new ArrayList<>());
        scaledImageBitmap = Bitmap.createScaledBitmap(imageBitmap, 1700, 400, false);
        scaledImageBitmap2=Bitmap.createScaledBitmap(imageBitmap2, 1700, 300, false);
        ed1=findViewById(R.id.username);
        ed2=findViewById(R.id.CourseName);
        ed3=findViewById(R.id.CertificateMarks);
        btnCreate = findViewById(R.id.creatpdf);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    createPDF();
                } else {
                    requestAllPermission();
                }
            }
        });

    }

    private void createPDF() {
      String  name= String.valueOf(ed1.getText());
        String courseName= String.valueOf(ed2.getText());
        String marks= String.valueOf(ed3.getText());
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint newpaint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(scaledImageBitmap, 0, 0, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        newpaint.setTextSize(35);
        newpaint.setTextAlign(Paint.Align.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            paint.setTypeface(Typeface.create(ResourcesCompat.getFont(getApplicationContext(),R.font.satisfy), Typeface.BOLD_ITALIC));
            newpaint.setTypeface(Typeface.create(ResourcesCompat.getFont(getApplicationContext(),R.font.satisfy), Typeface.ITALIC));
        }
        canvas.drawText("Student name : "+name, pageWidth / 2, 600, paint);
        canvas.drawText("CourseName : "+courseName, pageWidth / 2, 670, paint);
        canvas.drawText("Scored : "+marks, pageWidth / 2, 740, paint);
        canvas.drawText("This is to certify that "+name+" has been successfully completed the "+courseName+" course with "+marks+"marks.", pageWidth / 2, 800, newpaint);
        canvas.drawBitmap(scaledImageBitmap2, 0,1050, paint);
        pdfDocument.finishPage(page);

        String pdfPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/PDF_CERTIFICATE"+System.currentTimeMillis();
            File file =new File(pdfPath);
            if(!file.exists())
            {
                file.mkdirs();
            }
            File pdf_file=new File(file.getPath()+".pdf");
            Gson gson =new Gson();
            MySharedPrefs.getInstance(getApplicationContext()).putString(Constants.L4M_PdfFiles,gson.toJson(filenames.add(pdf_file.getPath())));
            if(!pdf_file.exists()) {
                try {
                    pdf_file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        try {
            pdfDocument.writeTo(new FileOutputStream(pdf_file));
            Toast.makeText(this, "PDF saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();


    }

    private void requestAllPermission() {

        ActivityCompat.requestPermissions(Certificate_Main_Activity.this, new String[]{READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Certificate_Main_Activity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}