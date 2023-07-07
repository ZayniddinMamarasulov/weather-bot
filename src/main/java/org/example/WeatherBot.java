package org.example;

import org.example.models.CurrentWeather;
import org.example.services.IconService;
import org.example.services.WeatherService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherBot extends TelegramLongPollingBot {

    WeatherService weatherService;
    IconService iconService;
    Map<Long, Location> locationMap;

    public WeatherBot(String botToken, WeatherService weatherService, IconService iconService) {
        super(botToken);
        this.weatherService = weatherService;
        this.iconService = iconService;
        locationMap = new HashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String userName = update.getMessage().getChat().getUserName();
            String text = update.getMessage().getText();
            printLogs(userName, text);
            SendMessage sendMessage = new SendMessage();
            Long chatId = update.getMessage().getChatId();
            sendMessage.setChatId(chatId);

            if (update.getMessage().hasLocation() || locationMap.get(chatId) != null) {
                Location location = new Location();

                if (update.getMessage().hasLocation()) {
                    location = update.getMessage().getLocation();
                    locationMap.put(chatId, location);
                } else if (locationMap.get(chatId) != null) {
                    location = locationMap.get(chatId);
                }

                CurrentWeather currentWeather = callWeatherService(location);

                String message = "——————————-\n" +
                        "\uD83D\uDCCD " + currentWeather.getName() + "\n" +
                        " \uD83C\uDF21 " + currentWeather.getMain().getTemp() + "\n" +
                        iconService.getEmoji(currentWeather.getWeather().get(0).getIcon())
                        + currentWeather.getWeather().get(0).getMain() + "\n" +
                        "——————————--";
                System.out.println(message);
                sendMessage.setText(message);
                executeMessage(sendMessage);
            }

            switch (text) {
                case "/start" -> {
                    sendMessage.setText("Salom " + userName + "\nservisdan foydalanish uchun lokatsiyani yuboring");

                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    List<KeyboardRow> keyboardRows = new ArrayList<>();
                    KeyboardRow keyboardRow1 = new KeyboardRow();
                    KeyboardButton keyboardButton = new KeyboardButton();
                    keyboardButton.setText("Share location");
                    keyboardButton.setRequestLocation(true);
                    keyboardRow1.add(keyboardButton);
                    keyboardRows.add(keyboardRow1);
                    replyKeyboardMarkup.setKeyboard(keyboardRows);
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);

                    executeMessage(sendMessage);
                }
                case "1" -> {
                    sendMessage.setText("siz 1 deb yozdingiz " + userName);
                    executeMessage(sendMessage);
                }
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return "weathers_uz_bot";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    private void printLogs(String userName, String text) {
        System.out.println(userName + "-----> " + text);
    }

    private CurrentWeather callWeatherService(Location location) {
        CurrentWeather currentWeather = weatherService
                .getMyWeather(location.getLongitude().toString(), location.getLatitude().toString());

        return currentWeather;
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
