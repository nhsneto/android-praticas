package pdm.nelson.pratica09;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class CityAdapter extends ArrayAdapter<City> {

    private City[] cities;
    private RequestQueue queue;
    private ImageLoader loader;

    static class ViewHolder {
        TextView cityName;
        TextView cityInfo;
        NetworkImageView weatherIcon;
    }

    public CityAdapter(Context context, int resource, City[] cities, RequestQueue queue) {
        super(context, resource, cities);
        this.cities = cities;
        this.queue = queue;

        this.loader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View listItem = null;
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            listItem = inflater.inflate(R.layout.city_listitem, null, true);

            holder = new ViewHolder();
            holder.cityName = listItem.findViewById(R.id.city_name);
            holder.cityInfo = listItem.findViewById(R.id.city_info);
            holder.weatherIcon = listItem.findViewById(R.id.weather_icon);

            listItem.setTag(holder);
        } else {
            listItem = view;
            holder = (ViewHolder) view.getTag();
        }

        holder.cityName.setText(cities[position].getName());

        if (cities[position].getWeather() != null) {
            holder.cityInfo.setText(cities[position].getWeather());
            holder.weatherIcon.setImageUrl(cities[position].getWeatherIconURL(), this.loader);
        } else {
            final City city = cities[position];
            final TextView weather = holder.cityInfo;
            weather.setText("Loading weather...");
            loadInBackground(holder, city);
        }

        return listItem;
    }

    private void loadInBackground(final ViewHolder holder, final City city) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.openweathermap.org");
        builder.appendPath("data/2.5/weather");
        builder.appendQueryParameter("q", city.getName());
        builder.appendQueryParameter("mode", "json");
        builder.appendQueryParameter("units", "metric");
        builder.appendQueryParameter("APPID", "a11ac945f2360e8cf7d496e7cb53dc00");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                builder.build().toString(), null,
                response -> {
                    String weatherStr = getWeather(response);
                    String weatherIconURL = getWeatherIconURL(response);

                    if (weatherStr != null) {
                        city.setWeather(weatherStr);
                        city.setWeatherIconURL(weatherIconURL);

                        holder.cityInfo.setText(weatherStr);
                        holder.weatherIcon.setImageUrl(weatherIconURL, this.loader);
                    } else {
                        holder.cityInfo.setText("Erro!");
                    }
                },
                error -> holder.cityInfo.setText("Erro!"));

        queue.add(request);
    }

    private String getWeather(JSONObject forecastJSON) {
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAIN = "main";
        final String OWM_DESCRIPTION = "description";

        try {
            JSONObject weatherObject = forecastJSON.getJSONArray(OWM_WEATHER).getJSONObject(0);
            JSONObject mainObject = forecastJSON.getJSONObject(OWM_MAIN);
            String description = weatherObject.getString(OWM_DESCRIPTION);
            double temp = mainObject.getDouble(OWM_TEMPERATURE);
            return description + " - " + temp;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getWeatherIconURL(JSONObject forecastJSON) {
        final String OWM_WEATHER = "weather";
        final String OWM_ICON = "icon";

        try {
            JSONObject weatherObject = forecastJSON.getJSONArray(OWM_WEATHER).getJSONObject(0);
            String iconCode = weatherObject.getString(OWM_ICON);
            return "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
