package pdm.pratica11;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
                context, 0, newIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = createChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText("Alarme disparou! Toque para reagendar.");
        builder.setContentIntent(pendingNotificationIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private String createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Pratica11";
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Canal Pratica11",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            return channelId;
        }

        return null;
    }
}
