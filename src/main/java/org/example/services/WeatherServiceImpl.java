package org.example.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.CurrentWeather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;

public class WeatherServiceImpl implements WeatherService {
    @Override
    public CurrentWeather getMyWeather(String lon, String lat) {
        try {

            URL url = new URL("https://api.openweathermap.org/data/2.5/weather" +
                    "?lat=" + lat +
                    "&lon=" + lon +
                    "&units=metric" +
                    "&appid=b18b0810c0b69f4ce19e8b2f8041446a");
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String row;
            StringBuilder stringBuilder = new StringBuilder();

            while ((row = bufferedReader.readLine()) != null) {
                stringBuilder.append(row);
            }

            Type typeToken = new TypeToken<CurrentWeather>() {
            }.getType();

            Gson gson = new Gson();
            CurrentWeather currentWeather = gson.fromJson(stringBuilder.toString(), typeToken);
            System.out.println("TEMP = " + currentWeather.getMain().getTemp());

            return currentWeather;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getWeather() {

    }
}
