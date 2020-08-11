package com.sidomafit.dosen.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sidomafit.dosen.MainActivity;
import com.sidomafit.dosen.activity.DetailMahasiswaActivity;
import com.sidomafit.dosen.menudrawer.NotifikasiFragment;

import static com.sidomafit.dosen.MainActivity.USER_ID;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if (USER_ID != null) {
            updateToken();
        }
    }

    private void updateToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String refereshToken = task.getResult().getToken();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
                        Token token = new Token(refereshToken);
                        reference.child(USER_ID).setValue(token);
                    }
                });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && sented.equals(USER_ID)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendOreoNotification(remoteMessage);
            } else {
                sendNotification(remoteMessage);
            }
        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        int i = 0;

        try {
            i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        } catch (NumberFormatException nfe) {
            Log.d("CEKDATA", nfe.toString());
        }
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(user, title);
        intent.putExtras(bundle);
        intent.putExtra("NotificationMessage", "notif");
        Log.d("CEKDATA", body);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification notification1 = new OreoNotification(getApplicationContext());
        Notification.Builder builder = notification1.getOreoNotification(title, body, pendingIntent, defaultSound, icon);


        int j = 0;
        if (i > 0) {
            j = i;
        }

        notification1.getManager().notify(j, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = 0;

        try {
            i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        } catch (NumberFormatException nfe) {
            Log.d("CEKDATA", nfe.toString());
        }

        Intent intent = new Intent(this, NotifikasiFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("hisUid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int j = 0;
        if (i > 0) {
            j = i;
        }

        noti.notify(j, builder.build());

    }
}
