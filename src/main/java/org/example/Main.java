package org.example;

import org.example.services.IconService;
import org.example.services.IconServiceImpl;
import org.example.services.WeatherService;
import org.example.services.WeatherServiceImpl;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {

        WeatherService weatherService = new WeatherServiceImpl();
        IconService iconService = new IconServiceImpl();
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            WeatherBot weatherBot = new WeatherBot(
                    "6179567475:AAGkQ2vhk-KyiMvP2OdFO4WX7ZU7d7HODBw",
                    weatherService,
                    iconService
            );

            telegramBotsApi.registerBot(weatherBot);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}