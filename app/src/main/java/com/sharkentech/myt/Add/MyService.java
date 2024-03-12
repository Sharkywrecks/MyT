package com.sharkentech.myt.Add;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.sharkentech.myt.R;
import com.sharkentech.myt.TrackFoodActivity;

public class MyService extends Service {
    private NotificationManager notificationManager;
    private Context context;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = getApplicationContext();
        System.out.println("Int carried was "+intent.getIntExtra("SelectedNoteId",-1));
        if(intent.getIntExtra("SelectedNoteId",-1) != -1){
            FoodTrackerNote selectedNote = FoodTrackerNote.getNoteForID(intent.getIntExtra("SelectedNoteId",-1));
            if(selectedNote!=null){
                showNotification(selectedNote);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {
        this.context = getApplicationContext();
        System.out.println("Int carried was "+service.getIntExtra("SelectedNoteId",-1));
        if(service.getIntExtra("SelectedNoteId",-1) != -1){
            FoodTrackerNote selectedNote = FoodTrackerNote.getNoteForID(service.getIntExtra("SelectedNoteId",-1));
            if(selectedNote!=null){
                showNotification(selectedNote);
            }
        }
        return super.startForegroundService(service);
    }

    public void showNotification(FoodTrackerNote selectedNote){
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent activityIntent = new Intent(context, TrackFoodActivity.class);
        PendingIntent activityPendingIntent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            activityPendingIntent = PendingIntent.getActivities(
                    context,
                    selectedNote.getId(),
                    new Intent[]{activityIntent},
                    PendingIntent.FLAG_MUTABLE);
        }else{
            activityPendingIntent = PendingIntent.getActivities(
                    context,
                    selectedNote.getId(),
                    new Intent[]{activityIntent},
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Notification notification = new NotificationCompat.Builder(context,FoodTrackerNotificationService.channelId)
                .setSmallIcon(R.drawable.myt)
                .setContentTitle("Food Tracker has detected...")
                .setContentText(selectedNote.getFoodName()+" is about to expire :(")
                .setContentIntent(activityPendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(selectedNote.getId(), notification);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
