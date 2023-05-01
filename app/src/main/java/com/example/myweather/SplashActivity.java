package com.example.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.Sky));

        TextView textView = findViewById(R.id.textTitleSplashScreen);
        textView.animate().alpha(1).setDuration(1300).setStartDelay(1100);
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isPermissionLocGranted = preferences.getBoolean("isPermissionLocGranted", false);
        boolean isPermissionNotifGranted = preferences.getBoolean("isPermissionNotifGranted", false);


        Thread thread = new Thread(){
            public void run(){
                try {
                    Thread.sleep(4450);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent;
                    if(isPermissionLocGranted && isPermissionNotifGranted){
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }
                    else{
                        intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }

            }
        };

        thread.start();

    }
}