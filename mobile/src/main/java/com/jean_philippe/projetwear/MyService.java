package com.jean_philippe.projetwear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyService extends Service implements SensorEventListener {

    public static final String SPORT_TYPE = "sportType";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    SensorManager sensorManager;
    Sensor heartRate, linearAccelearion, bodyTemperature, stepCounter;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {

                switch (action) {
                    case ACTION_START_FOREGROUND_SERVICE:
                        String type = intent.getStringExtra(SPORT_TYPE);
                        if (type != null) {
                            initializeSensors();
                            startForegroundService(type);
                            Toast.makeText(getApplicationContext(), R.string.using_sensors, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ACTION_STOP_FOREGROUND_SERVICE:

                        Toast.makeText(getApplicationContext(),
                                R.string.sensor_usage_stopped, Toast.LENGTH_LONG).show();
                        stopForegroundService();
                        break;


                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initializeSensors() {
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        heartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        linearAccelearion = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bodyTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }

        if (heartRate != null) {

            sensorManager.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (bodyTemperature != null) {

            sensorManager.registerListener(this, bodyTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (stepCounter != null) {

            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (linearAccelearion != null) {

            sensorManager.registerListener(this, linearAccelearion, SensorManager.SENSOR_DELAY_NORMAL);
        }


    }


    private void startForegroundService(String type) {

        //Service can be start on wear if we make wear app


        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationsChannelx.CHANNEL_1_ID);
        builder.setSmallIcon(R.drawable.sports)
                .setContentTitle(getString(R.string.using_sensors))
                .setContentText(type);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }


        // Add Pause button intent in notification.
        Intent pauseIntent = new Intent(this, MyService.class);
        pauseIntent.setAction(ACTION_STOP_FOREGROUND_SERVICE);
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, getString(R.string.cancel), pendingPrevIntent);
        builder.addAction(prevAction);

        // Build the notification.
        Notification notification = builder.build();

        // Start foreground service.
        startForeground(1, notification);
    }

    private void stopForegroundService() {


        // Stop foreground service and remove the notification.
        stopForeground(true);
        sensorManager.unregisterListener(this);


        stopSelf();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        try {
            switch (sensorEvent.sensor.getType()) {
                case Sensor.TYPE_HEART_RATE:

                    float heartRateData = sensorEvent.values[0];
                    //we can save this value in database or any other or just show it simultaneously

                    break;

                case Sensor.TYPE_LINEAR_ACCELERATION:

                    //Acceleration along x axis y axis and z axis
                    //note that gyroscope or linear acceleration is not enough to measure speed
                    //we can use linear acceleration with gps to measure accurate speed and by using filtering
                    float axisX = sensorEvent.values[0];
                    float axisY = sensorEvent.values[1];
                    float axisZ = sensorEvent.values[2];
                    break;

                case Sensor.TYPE_AMBIENT_TEMPERATURE:

                    //this is in Centigrade
                    float bodyTemperatureData = sensorEvent.values[0];
                    //we can save this value in database or any other or just show it simultaneously
                    break;

                case Sensor.TYPE_STEP_DETECTOR:

                    //its only detects step so it can be save in file or database or in shared Preference
                    break;


            }
        } catch (Exception e) {

            //may throw excpetion because of heart rate and temperature is not define in min sdk version
            //Exception will be ignored
            Log.e(TAG_FOREGROUND_SERVICE, " :OnSensorChanged:  " + e.getMessage());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}