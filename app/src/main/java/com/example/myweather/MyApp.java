package com.example.myweather;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel = new NotificationChannel("weather", "Weather", NotificationManager.IMPORTANCE_HIGH);
        NotificationChannel channel1 = new NotificationChannel("alert", "Alerts", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        notificationManager.createNotificationChannel(channel1);
    }
}
