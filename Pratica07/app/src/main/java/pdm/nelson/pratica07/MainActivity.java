package pdm.nelson.pratica07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ForecastTask.ForecastListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ok = findViewById(R.id.button_ok);
        ok.setOnClickListener(view -> {
            String cityName = ((EditText) findViewById(R.id.edit_city)).getText().toString();
            new ForecastTask(this).execute(cityName);
        });
    }

    @Override
    public void showForecast(List<String> forecast) {
        if (forecast == null) {
            String cityName = ((EditText) findViewById(R.id.edit_city)).getText().toString();
            Toast toast = Toast.makeText(this, "Previsão não encontrada para " + cityName, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            ArrayList<CharSequence> data = new ArrayList<>(forecast);
            Intent intent = new Intent(this, ForecastActivity.class);
            intent.putCharSequenceArrayListExtra("data", data);
            startActivity(intent);
        }
    }
}
