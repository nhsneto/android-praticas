package pdm.nelson.pratica08;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherForecastAPI {

    String APPID = "a11ac945f2360e8cf7d496e7cb53dc00";

    @GET("forecast/daily?APPID=" + APPID + "&mode=json&units=metric&cnt=7")
    Call<WeatherForecast> getForecast(@Query("q") String city);
}
