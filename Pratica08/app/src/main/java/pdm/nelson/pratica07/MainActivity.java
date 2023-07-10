package pdm.nelson.pratica07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ForecastTask.ForecastListener {

    WeatherForecastAPI forecastAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        forecastAPI = retrofit.create(WeatherForecastAPI.class);

        Button ok = findViewById(R.id.button_ok);
        ok.setOnClickListener(view -> {
            final String cityName = ((EditText) findViewById(R.id.edit_city)).getText().toString();

            Call<WeatherForecast> call = forecastAPI.getForecast(cityName);
            call.enqueue(new Callback<WeatherForecast>() {
                @Override
                public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                    WeatherForecast forecast = response.body();
                    List<String> forecastList = null;

                    if (forecast != null) {
                        GregorianCalendar cal = new GregorianCalendar();
                        forecastList = new ArrayList<>();
                        for (WeatherForecast.Forecast fc : forecast.list) {
                            String tempStr = ForecastParser.formatHighLows(fc.temp.min, fc.temp.max);
                            String dateStr = ForecastParser.getReadableDateString(cal.getTimeInMillis());
                            String fcStr = dateStr + " - " + fc.weather.get(0).description + " - " + tempStr;
                            forecastList.add(fcStr);
                            cal.add(GregorianCalendar.DATE, 1);
                        }
                    }

                    showForecast(forecastList);
                }

                @Override
                public void onFailure(Call<WeatherForecast> call, Throwable t) {
                    Toast toast = Toast.makeText(MainActivity.this,
                            "Previsão não encontrada para " + cityName, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
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
