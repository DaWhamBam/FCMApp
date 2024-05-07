package com.example.messagingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       btn1 = findViewById(R.id.btn1);
       btn2 = findViewById(R.id.btn2);


       if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
           String channelId = getString(R.string.default_notification_channel_id);
           String channelName = getString(R.string.default_notification_channel_name);

           NotificationManager notificationManager = getSystemService(NotificationManager.class);
           notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, notificationManager.IMPORTANCE_LOW));
       }

       btn1 = findViewById(R.id.btn1);
       btn2 = findViewById(R.id.btn2);

       if (getIntent().getExtras() != null) {
           for (String key : getIntent().getExtras().keySet()) {
               Object value = getIntent().getExtras().get(key);
               Log.d(TAG, "Key: " +key + " Value: " +value);
           }
       }


       btn1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FirebaseMessaging.getInstance().subscribeToTopic("weather")
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               String msg = "Subscribed!";
                               if (!task.isSuccessful()) {
                                   msg = "Failed!";
                               }
                               Log.d(TAG, msg);
                               Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

                           }
                       });
           }
       });

       btn2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                   @Override
                   public void onComplete(@NonNull Task<String> task) {
                       if (!task.isSuccessful()) {
                           Log.v(TAG, "Failed to register Token", task.getException());
                       }

                       String token = task.getResult();
                       String msg = getString(R.string.msg_token_fmt, token);

                       Log.v(TAG, msg);
                       Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                   }
               });
           }
       });



    }
}