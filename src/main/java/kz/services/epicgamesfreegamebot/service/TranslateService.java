package kz.services.epicgamesfreegamebot.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class TranslateService {

    @Value("${yandex.folder-id}")
    private String FOLDER_ID;

    @Value("${yandex.api-url}")
    private String API_URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";

    private static final String TARGET_LANGUAGE = "ru";

    public String translateText(String[] texts) {
        try {
            String IAM_TOKEN = getIamToken();
            if (IAM_TOKEN.isEmpty()) {
                System.err.println("Failed to obtain IAM token.");
                throw new RuntimeException("Failed to obtain IAM token");
            }

            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + IAM_TOKEN);
            connection.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("targetLanguageCode", TARGET_LANGUAGE);
            json.put("folderId", FOLDER_ID);
            json.put("texts", new JSONArray(texts));

            String jsonInputString = json.toString();
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                return parseTranslationResponse(
                        new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                );
            } else throw new RuntimeException("HTTP error code : " + code);

        } catch (Exception e) {
            //
        }

        return "Fail";
    }


    private static String getIamToken() {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("yc", "iam", "create-token");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) output.append(line);

            process.waitFor();
        } catch (Exception exc) {
            //Исключения
        }

        return output.toString().trim();
    }

    private static String parseTranslationResponse(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray translations = jsonResponse.getJSONArray("translations");
        if (translations.length() > 0) {
            return translations.getJSONObject(0).getString("text");
        }
        return null;
    }
}
