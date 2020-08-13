package com.sidomafit.mahasiswa.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

class OreoNotification extends ContextWrapper {

    private static final  String CHANNEL_ID = "com.sidomafit.dosen";
    private static final String  CHANNEL_NAME ="sidoma";

    private NotificationManager notificationManager;

    public OreoNotification(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel1() ;
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel1() {

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);

    }

    public NotificationManager getManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public  Notification.Builder getOreoNotification(String title, String body,
                                                     PendingIntent pendingIntent,
                                                     Uri soundUri,
                                                     String icon){
        return  new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setSmallIcon(Integer.parseInt(icon));
    }
}
