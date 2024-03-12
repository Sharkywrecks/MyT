package com.sharkentech.myt.Add;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.sharkentech.myt.CodeScannerActivity;
import com.sharkentech.myt.R;
import com.sharkentech.myt.TrackFoodActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Calendar;

public class FoodTrackerNotificationService{
    private Context context;
    public static final String channelId = "FoodTracker";
    //private AlarmBroadcastReceiver receiver;
    private IntentFilter intentFilter;
    public FoodTrackerNotificationService(Context context){
        this.context = context;
        //receiver = new AlarmBroadcastReceiver();
        ComponentName myReceiver = new ComponentName(context,AlarmBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(myReceiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        /*intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);*/
        /*intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);*/
        //intentFilter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        //intentFilter = new IntentFilter("com.sharkentech.myt.Add.action.trigger");
        //context.registerReceiver(receiver,intentFilter);
    }
    public void startAlarmBroadcastReceiver(FoodTrackerNote selectedNote, int day, int month, int year) {
        Intent activityIntent = new Intent(context, AlarmBroadcastReceiver.class);
        //activityIntent.setAction(Intent.ACTION_BOOT_COMPLETED);
        activityIntent.setClass(context,AlarmBroadcastReceiver.class);
        activityIntent.setAction("com.sharkentech.myt.Add.action.trigger");
        activityIntent.putExtra("SelectedNoteId",selectedNote.getId());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try{
            out = new ObjectOutputStream(bos);
            out.writeObject(selectedNote.getId());
            out.flush();
            byte[] data = bos.toByteArray();
            activityIntent.putExtra("SelectedNoteIdByte",data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*Bundle bundle = new Bundle();
        bundle.putInt("SelectedNoteId",selectedNote.getId());
        activityIntent.putExtra("bundle",bundle);*/
        PendingIntent activityPendingIntent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            activityPendingIntent = PendingIntent.getBroadcast/*getActivities*/(
                    context,
                    selectedNote.getId(),
                    activityIntent,
                    PendingIntent.FLAG_IMMUTABLE);
        }else{
            activityPendingIntent = PendingIntent.getBroadcast/*getActivities*/(
                    context,
                    selectedNote.getId(),
                    activityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long time = getTime(day, month,year);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC,time,activityPendingIntent);
        //alarmManager.setAndAllowWhileIdle(AlarmManager.RTC,time,activityPendingIntent);
    }
    private long getTime(int day, int month, int year) {
        int minute = 0;
        int hour = 14;

        Calendar calendar = Calendar.getInstance();
        System.out.println(year+" "+month+" "+day);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month-1);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.add(Calendar.DATE,-7);
        System.out.println("Alarm time set is "+calendar.getTime());
        return calendar.getTimeInMillis();
    }
    public void cancelAlarmIfExists(FoodTrackerNote selectedNote) {
        try{
            Intent activityIntent = new Intent(context, AlarmBroadcastReceiver.class);
            activityIntent.putExtra("SelectedNoteId",selectedNote.getId());
            /*Bundle bundle = new Bundle();
            bundle.putInt("SelectedNoteId",selectedNote.getId());
            activityIntent.putExtra("bundle",bundle);*/
            PendingIntent activityPendingIntent;

        activityPendingIntent = PendingIntent./*getBroadcast*/getService(
                context,
                selectedNote.getId(),
                activityIntent,
                PendingIntent.FLAG_NO_CREATE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(activityPendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*private void showAlert(long time, String title, String text) {
        //Date date = new Date(time);
        DateFormat dateFormat = DateFormat.getDateInstance();
        new AlertDialog.Builder(CodeScannerActivity.this).setTitle(title)
                .setMessage(text+" is about to expire on "+dateFormat.format(time))
                .setIcon(R.drawable.myt).show();
    }*/

}
