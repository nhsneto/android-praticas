package pdm.pratica11;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private static final int MY_POST_NOTIFICATIONS = 11;
    private boolean isPostNotificationsGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.isPostNotificationsGranted = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        }

        Button scheduleButton = findViewById(R.id.scheduleButton);
        scheduleButton.setOnClickListener(this::scheduleAlarm);
    }

    public void scheduleAlarm(View view) {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(
                this, 1, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long alarmTimeInMillis = new GregorianCalendar().getTimeInMillis() + 6 * 1000;
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, alarmPendingIntent);

        Toast.makeText(this, "Alarme agendado.", Toast.LENGTH_LONG).show();

        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNotificationPermission() {
        this.isPostNotificationsGranted = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;

        if (!isPostNotificationsGranted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    MY_POST_NOTIFICATIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0) {
            return;
        }

        if (requestCode == MY_POST_NOTIFICATIONS) {
            this.isPostNotificationsGranted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }
}
