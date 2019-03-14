package be.kdg.mobile_client.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import be.kdg.mobile_client.MainActivity;
import be.kdg.mobile_client.R;
import lombok.NoArgsConstructor;

/**
 * Simple config to see the notifications that come into the application.
 */
@NoArgsConstructor
public class FirebaseMessagingServiceImpl extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";

    /**
     * Will be called when a notification is received.
     *
     * @param remoteMessage The message that comes from the google remote.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();

            Log.d(TAG, "Message notification title: " + title);
            Log.d(TAG, "Message notification body: " + message);

            sendNotification(title, message);
        }
    }

    /**
     * Sends the notification to the users phone
     *
     * @param title       The title of the notification.
     * @param messageBody The message of the notification.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
