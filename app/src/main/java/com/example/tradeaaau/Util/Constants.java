package com.example.tradeaaau.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.example.tradeaaau.R;


public class Constants {
    public static String L4M_AccountStatus="AccountLogin";
    public static String L4M_Username="Username";
    public static String L4M_Email="Email";
    public static String L4M_City="City";
    public static String L4M_Mobilenumber="Mobilenumber";
    public static String L4M_PdfFiles="L4M_PdfFiles";
    public static String L4M_Favourite="Favourite";
    public static String L4M_Country="Country";
    public static String L4M_UserId="UserId";
    public static String L4M_user_id="user_id";
    public static String L4M_QuizId="quiz_Id";
    public static String L4M_Quizid="quiz_id";

    public static void alertWithIntent(final Context context, String msg, final Class className)
    {
        final Dialog dialog = new Dialog(context);
//        dialog.setTitle("FIN-CEX");
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
        tvText.setText(msg);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, className);
                context.startActivity(intent);
                dialog.dismiss();

            }
        });

    }

    public static void alertDialog(Context context, String str)
    {
        final Dialog dialog = new Dialog(context);
//        dialog.setTitle("FIN-CEX");
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

        tvText.setText(str);

        btnok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // moveTaskToBack(true);
                dialog.dismiss();
            }
        });


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
