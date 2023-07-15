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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
