package com.jean_philippe.projetwear;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import static com.jean_philippe.projetwear.AddTask.REQEST_CODE_FILE;
import static com.jean_philippe.projetwear.NotificationsChannelx.CHANNEL_1_ID;

public class RecepteurAlarme extends BroadcastReceiver {
    public static final String resultKeyForNotification = "myResultKey01";
    public static final String sportsKeyForNotification = "sportsResultKey2";

    private NotificationManagerCompat notificationManager;

    @Override

    public void onReceive(Context context, Intent intent) {

        notificationManager = NotificationManagerCompat.from(context);


        String name = intent.getStringExtra(AddTask.PROFILE_KEY);
        boolean isSportsCategory = intent.getBooleanExtra(AddTask.CATEGORY_KEY, false);
        if (name == null)
            this.abortBroadcast();


        createNotification(name, context, isSportsCategory);


    }


    private void createNotification(String name, Context context, boolean isSportsCategory) {

        RemoteInput remoteInput = new RemoteInput.Builder(resultKeyForNotification)
                .setLabel(context.getString(R.string.voice_input))
                .setChoices(context.getResources().getStringArray(R.array.choices))
                .build();


        NotificationCompat.Action postponeAndVoiceAction =
                new NotificationCompat.
                        Action.Builder(R.drawable.ic_voice,
                        context.getString(R.string.voice_input), buildPendingIntent(context, name
                        , isSportsCategory))
                        .addRemoteInput(remoteInput)

                        .build();


        NotificationCompat.WearableExtender extended = new NotificationCompat.WearableExtender()

                .addAction(postponeAndVoiceAction);

        int notificationId = getIntegerName(name, context);
        NotificationCompat.Builder normal = buildNormal(context, name);

        normal.extend(extended);


        notificationManager.notify(notificationId,
                normal.build());


    }

    public int getIntegerName(String oldNameFinal, Context context) {


        SharedPreferences pref = context.getSharedPreferences(REQEST_CODE_FILE, Context.MODE_PRIVATE);


        return pref.getInt(oldNameFinal, 0);


    }

    private NotificationCompat.Builder buildNormal(Context context, String name) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(context, CHANNEL_1_ID);
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(name)
                .setContentText(context.getString(R.string.reminder))
                .setWhen(System.currentTimeMillis())
                //.setContentIntent(buildPendingIntent(Settings.ACTION_SECURITY_SETTINGS))
                .setSmallIcon(R.drawable.todo);
        return (b);
    }


    private PendingIntent buildPendingIntent(Context ctxt, String name, boolean isSupportCategory) {


        Intent i = new Intent(ctxt, DonneesWear.class);
        i.putExtra(AddTask.PROFILE_KEY, name);
        i.putExtra(AddTask.CATEGORY_KEY, isSupportCategory);

        return PendingIntent.getBroadcast(ctxt, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}