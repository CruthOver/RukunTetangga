package id.akhir.proyek.rukuntetangga.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import id.akhir.proyek.rukuntetangga.MainActivity;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;

public class MyFirebaseService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Message Firebase", remoteMessage.getNotification().getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendOreoNotification(remoteMessage);
        } else {
            sendNotification(remoteMessage);
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("FCM TOKEN ", s);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token) {
        AppSession session = new AppSession(this);
        session.setFcmToken(token);
    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {
//        String user = remoteMessage.getData().get("user");
//        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));

        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putString("userId", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultSound, R.drawable.ic_stat_ic_notification);

//        int i = 0;
//        if (j > 0) {
//            i = j;
//        }
        oreoNotification.getManager().notify(0, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {
//        String user = remoteMessage.getData().get("user");
//        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        Log.d("Notification", title);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        int j = 1;

        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0) {
            i = j;
        }
        notificationManager.notify(i, builder.build());
    }
}
