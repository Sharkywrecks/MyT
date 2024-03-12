package com.sharkentech.myt.Add;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.sharkentech.myt.MainActivity;
import com.sharkentech.myt.R;
import com.sharkentech.myt.TrackFoodActivity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Objects;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private NotificationManager notificationManager;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        System.out.println(intent.getAction());
        if(intent.getAction().equals( "com.sharkentech.myt.Add.action.trigger")) {
        ByteArrayInputStream bis = new ByteArrayInputStream(intent.getByteArrayExtra("SelectedNoteIdByte"));
        ObjectInput in = null;
        int selectedNoteId = -1;
        try{
            in = new ObjectInputStream(bis);
            selectedNoteId = (int) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(selectedNoteId == -1){
            selectedNoteId = intent.getExtras().getInt("SelectedNoteId", -1);
        }

            if (selectedNoteId == -1) {
                selectedNoteId = 0;
                System.out.println("Failed to carry int");
            }
            Intent serviceIntent = new Intent(context, MyService.class);
            serviceIntent.putExtra("SelectedNoteId", selectedNoteId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
                /*FoodTrackerNote selectedNote = FoodTrackerNote.getNoteForID(0);
                showNotification(selectedNote);*/
            } else {
                /*FoodTrackerNote selectedNote = FoodTrackerNote.getNoteForID(0);
                showNotification(selectedNote);*/
                context.startService(serviceIntent);
            }
        }
        //}
        /*if(selectedNoteId != -1){
            FoodTrackerNote selectedNote = FoodTrackerNote.getNoteForID(0);
            System.out.println("Selected note name was "+selectedNote.getFoodName());
            if(selectedNote!=null){
                showNotification(selectedNote);
            }
        }*/
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
                    PendingIntent.FLAG_IMMUTABLE);
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

        /*System.out.println("Int carried was "+intent.getIntExtra("SelectedNoteId",-1));
        if(intent.getIntExtra("SelectedNoteId",-1) != -1){
            FoodTrackerNote selectedNote = FoodTrackerNote.getNoteForID(intent.getIntExtra("SelectedNoteId",-1));
            if(selectedNote!=null){
                showNotification(selectedNote);
            }
        }*/
    }
/*
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
    }*/