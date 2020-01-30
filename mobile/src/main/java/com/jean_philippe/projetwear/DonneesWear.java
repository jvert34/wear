package com.jean_philippe.projetwear;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.RemoteInput;

import java.util.Calendar;
import java.util.Objects;

public class DonneesWear extends BroadcastReceiver {
    @Override
    public void onReceive(Context ctxt, Intent i) {
        Bundle input= RemoteInput.getResultsFromIntent(i);

        if (input!=null)
        { CharSequence resultsFromWear=input.getCharSequence(RecepteurAlarme.resultKeyForNotification);


            if (resultsFromWear!=null)
            {



                if(resultsFromWear.toString().equals("reporter le rappel")){


                    setAlarm(i,ctxt);


                }else {


                    ctxt.startActivity(new Intent(ctxt,AddTask.class));

                }



            }


            else {

                resultsFromWear=input.getCharSequence(RecepteurAlarme.sportsKeyForNotification);
                if(resultsFromWear!=null){



                    //Logic to start sport activity(forground service to record data)






                    Intent intent = new Intent(ctxt, MyService.class);
                    intent.putExtra(MyService.SPORT_TYPE,resultsFromWear.toString());
                    intent.setAction(MyService.ACTION_START_FOREGROUND_SERVICE);
                    ctxt.startService(intent);


                }



            }


        } else  { Log.e(getClass().getSimpleName(), "No voice response Bundle"); }
    }

    @SuppressLint("NewApi")
    public void setAlarm(Intent i, Context context) {



        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MINUTE,TaskHome.TASK_POSTPONE_MINUTES);

        Intent intent=new Intent(context,RecepteurAlarme.class);
        intent.putExtra(AddTask.PROFILE_KEY,i.getStringExtra(AddTask.PROFILE_KEY));
        intent.putExtra(AddTask.CATEGORY_KEY,i.getStringExtra(AddTask.CATEGORY_KEY));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, saveInteger(intent.getStringExtra(AddTask.PROFILE_KEY)
                ,context), intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        if (am != null) {
            try {


                am.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent), pendingIntent);

            } catch (Exception e) {

                Log.e(getClass().getSimpleName(), Objects.requireNonNull(e.getLocalizedMessage()));

            }


        }
    }

    public int saveInteger(String nameFinal,Context context){
        SharedPreferences pref = context.getSharedPreferences(AddTask.REQEST_CODE_FILE, Context.MODE_PRIVATE);

        int num=  pref.getInt(AddTask.CODE,0);

        if(num>1147483647)
            num=0;

        num++;

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(AddTask.CODE,num);
        editor.putInt(nameFinal,num);
        editor.apply();

        return num;
    }
}
