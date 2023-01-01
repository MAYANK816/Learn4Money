package com.example.tradeaaau.adapter;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.example.tradeaaau.Util.Constants;
import com.example.tradeaaau.Util.MySharedPrefs;
import com.example.tradeaaau.R;
import java.util.List;

public class VideoLessonAdapter extends RecyclerView.Adapter<VideoLessonAdapter.CourseViewHolder> {

    Context context;
    List<String> lessons;
    UserVideoVIew userVideoVIew;
    BookMarkInterface bookMarkInterface;

    public VideoLessonAdapter(Context context, List<String> lessons, UserVideoVIew userVideoVIew, BookMarkInterface bookMarkInterface) {
        this.context = context;
        this.lessons = lessons;
        this.userVideoVIew=userVideoVIew;
        this.bookMarkInterface=bookMarkInterface;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_lesson_row_items, parent, false);
        return new CourseViewHolder(view,userVideoVIew);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        int i=position+1;
        holder.contentNumber.setText("0"+i);
        if(lessons.get(position).length()>30)
            holder.contentName.setText(lessons.get(position).substring(0,28)+".....");
        else
            holder.contentName.setText(lessons.get(position));

        holder.bookmarkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                EditText editText=new EditText(context);
                builder.setTitle("Want to save a note ?");
                builder.setView(editText);
                // Show the Alert Dialog box
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(TextUtils.isEmpty(editText.getText()))
                        {
                            Toast.makeText(context,"Please Enter Your Note",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            bookMarkInterface.SaveBookmark(editText.getText().toString());
                            dialogInterface.cancel();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        holder.playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("courseAdapter", lessons.get(position) );
                String url=lessons.get(position);
               userVideoVIew.playVideo(url);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setMessage("Would you like to continue your learning ?");
//                // add the buttons
//                builder.setPositiveButton("Favourite", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Gson gson =new Gson();
//                        MySharedPrefs.getInstance(context.getApplicationContext()).addTask(Quiz_Constants.L4M_Favourite,lessons.get(position));
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                // create and show the alert dialog
//                AlertDialog dialog = builder.create();
//                dialog.show();
//

                return true;

            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }


    public static class CourseViewHolder extends RecyclerView.ViewHolder{


        TextView contentNumber, contentTime, contentName;
        UserVideoVIew userVideoVIew;
        ImageView playbutton,bookmarkImage;

        public CourseViewHolder(@NonNull View itemView,UserVideoVIew userVideoVIew) {
            super(itemView);
            this.userVideoVIew=userVideoVIew;
            contentName = itemView.findViewById(R.id.content_title);
            contentTime = itemView.findViewById(R.id.content_time);
            contentNumber = itemView.findViewById(R.id.content_number);
            playbutton=itemView.findViewById(R.id.imageView3);
            bookmarkImage=itemView.findViewById(R.id.bookmarkImage);
        }
    }
      public interface UserVideoVIew{
        void playVideo(String id);
        }
        public interface BookMarkInterface{
        void SaveBookmark(String videoLink);
        }
}
