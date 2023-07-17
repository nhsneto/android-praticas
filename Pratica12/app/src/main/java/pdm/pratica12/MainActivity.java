package pdm.pratica12;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_READ_EXTERNAL_STORAGE = 12;
    private static final int MY_WRITE_EXTERNAL_STORAGE = 13;
    private EditText urlEditText;
    private boolean permissionRead;
    private boolean permissionWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.urlEditText = findViewById(R.id.edit_url);
        this.permissionRead = false;
        this.permissionWrite = false;

        Button downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(this::downloadClick);

        requestPermission();
        notificationPermission();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public void downloadClick(View view) {
        if (!this.permissionRead && !this.permissionWrite) {
            Toast.makeText(this, "Erro! Permissões negadas",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String url = urlEditText.getText().toString();
        String filename = url.substring(url.lastIndexOf("/") + 1);
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.URL_PATH, url);
        intent.putExtra(DownloadService.FILENAME, filename);
        startService(intent);

        Toast.makeText(this, "Download iniciado.", Toast.LENGTH_LONG).show();

        this.finish();
    }

    private void requestPermission() {
        this.permissionRead = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        this.permissionWrite =
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!this.permissionRead) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_STORAGE);
        }
        if (!this.permissionWrite) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void notificationPermission() {
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.areNotificationsEnabled();
        NotificationChannel channel = new NotificationChannel(
                "canal", "Canal de Notificacao", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel); // Pede ao usuario a permissao para enviar notificacoes
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0) {
            return;
        }

        switch (requestCode) {
            case MY_WRITE_EXTERNAL_STORAGE:
                this.permissionWrite = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;
            case MY_READ_EXTERNAL_STORAGE:
                this.permissionRead = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;
            default:
        }
    }
}
