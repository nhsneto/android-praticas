package pdm.nelson.pratica09;

public class City {

    private String name;
    private String weather;
    private String weatherIconURL;

    public City(String name) {
        this.name = name;
        this.weather = null;
    }

    public String getName() {
        return name;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeatherIconURL() {
        return weatherIconURL;
    }

    public void setWeatherIconURL(String weatherIconURL) {
        this.weatherIconURL = weatherIconURL;
    }
}
