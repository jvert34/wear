package com.jean_philippe.projetwear;

import android.app.Notification;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    // private String NotificationID="channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
    }

    public void notifyme(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ch1");
        builder.setContentTitle("Title");
        builder.setContentText("Content");
        builder.setSmallIcon(R.drawable.ic_launcher_background);

        Notification notification = builder.build();
        NotificationManagerCompat managercompat = NotificationManagerCompat.from(this);
        managercompat.notify(1, builder.build());
    }
}
