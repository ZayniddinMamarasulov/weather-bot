package org.example.services;

import org.example.models.CurrentWeather;

public interface WeatherService {
    CurrentWeather getMyWeather(String lon, String lat);

    void getWeather();
}
