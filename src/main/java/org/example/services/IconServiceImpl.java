package org.example.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.CurrentWeather;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class IconServiceImpl implements IconService {

    @Override
    public String getEmoji(String iconCode) {
        File file = new File("src/main/resources/icons.json");
        System.out.println(file.getAbsolutePath());
        InputStream inputStream = null;
        Map<String, String> iconsMap = new HashMap<>();
        try {
            inputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String row;
            StringBuilder stringBuilder = new StringBuilder();

            while ((row = bufferedReader.readLine()) != null) {
                stringBuilder.append(row);
            }

            Type typeToken = new TypeToken<Map<String, String>>() {
            }.getType();

            Gson gson = new Gson();
            iconsMap = gson.fromJson(stringBuilder.toString(), typeToken);
        } catch (Exception e) {

        }

        return iconsMap.get(iconCode);
    }
}
