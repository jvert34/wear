package com.jean_philippe.projetwear;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class NotificationsChannelx extends Application {

    public static final String CHANNEL_1_ID = "ch1";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID,
                "ToDoTask Notification",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel1.setDescription("Notification of ToDoTask");

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel1);
        }
    }
}