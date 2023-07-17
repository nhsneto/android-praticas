package pdm.pratica12;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends IntentService {

    private static final String TAG = "DownloadService";

    public static final String URL_PATH = "urlpath";
    public static final String FILENAME = "filename";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "DownloadService onHandleIntent");

        String urlPath = intent.getStringExtra(URL_PATH);
        String fileName = intent.getStringExtra(FILENAME);
        String contentType = "";
        boolean success = false;

        File output = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), fileName);

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            contentType = connection.getContentType();
            inputStream = new BufferedInputStream(connection.getInputStream());
            fileOutputStream = new FileOutputStream(output.getPath());

            Log.i(TAG, "DownloadService downdloading...");

            int next;
            byte[] BUFFER = new byte[512];
            while ((next = inputStream.read(BUFFER, 0, 512)) != -1) {
                fileOutputStream.write(BUFFER, 0, next);
            }
            success = true;

            Log.i(TAG, "DownloadService finished downloading.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        publishResults(fileName, output.getAbsolutePath(), contentType, success);
    }

    private void publishResults(String fileName,
                                String filepath,
                                String contentType,
                                boolean success) {
        Intent newIntent;
        String msg;
        if (success) {
            Uri uri = Uri.parse("file://" + filepath);
            newIntent = new Intent(Intent.ACTION_VIEW);
            newIntent.setDataAndType(uri, contentType);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            msg = "Download completo: " + fileName;
        } else {
            newIntent = new Intent(this, MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            msg = "Download falhou: " + filepath;
        }

        showNotification(newIntent, msg);
    }

    private void showNotification(Intent intent, String message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String channelID = createChannel(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText(message);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private String createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Pratica12";
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Canal Pratica12",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            return channelId;
        }

        return null;
    }
}
