package kz.services.epicgamesfreegamebot.service;

import kz.services.epicgamesfreegamebot.model.Game;
import kz.services.epicgamesfreegamebot.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class FreeGameService {
    private final GameRepository gameRepository;
    private final TranslateService translateService;

    public void getFreeGames() throws IOException {
        URL url = new URL("https://store-site-backend-static.ak.epicgames.com/freeGamesPromotions?locale=ru&country=KZ&allowCountries=KZ");
        Scanner scanner = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();

        while (scanner.hasNext()){
            result.append(scanner.nextLine());
        }

        JSONObject object = new JSONObject(result.toString());

        JSONArray elements = object
                .getJSONObject("data")
                .getJSONObject("Catalog")
                .getJSONObject("searchStore")
                .getJSONArray("elements");


        if (!elements.isEmpty()) {

            for (int i = 0; i < elements.length(); i++) {
                if (!checkValid(elements.getJSONObject(i))) continue;

                Game game = Game.builder()
                        .title(elements.getJSONObject(i).getString("title"))
                        .description(elements.getJSONObject(i).getString("description"))
                        .imageUrl(parseImageUrl(elements.getJSONObject(i).getJSONArray("keyImages")))
                        .price(getPrice(elements.getJSONObject(i).getJSONObject("price")))
                        .startDate(parseDateTime(elements.getJSONObject(i), "startDate"))
                        .endDate(parseDateTime(elements.getJSONObject(i), "endDate"))
                        .build();

                if (game.getStartDate() == null || game.getEndDate() == null) continue;

                String[] des = { game.getDescription() };
                game.setDescription(translateService.translateText(des));

                if (!gameRepository.existsByTitle(game.getTitle())) gameRepository.save(game);
            }
        }
    }

    private static boolean checkValid(JSONObject object) {
        if (!object.has("promotions") || object.isNull("promotions")) return false;

        JSONArray promotionalOffersArray = new JSONArray();

        // Проверка наличия "promotionalOffers" и "upcomingPromotionalOffers"
        if (object.has("promotions")) {
            JSONObject promotions = object.getJSONObject("promotions");
            if (promotions.has("promotionalOffers")) {
                promotionalOffersArray = promotions.getJSONArray("promotionalOffers");
            }
            if (promotionalOffersArray.isEmpty() && promotions.has("upcomingPromotionalOffers")) {
                promotionalOffersArray = promotions.getJSONArray("upcomingPromotionalOffers");
            }
        }
        else return false;

        // Проверка, что массив содержит элементы
        if (promotionalOffersArray.isEmpty()) {
            throw new JSONException("No promotional offers found");
        }

        // Получение первого элемента "promotionalOffers"
        JSONObject promotionalOffer = promotionalOffersArray.getJSONObject(0);
        JSONArray promotionalOffersInnerArray = promotionalOffer.getJSONArray("promotionalOffers");

        // Проверка, что внутренний массив содержит элементы
        if (promotionalOffersInnerArray.isEmpty()) {
            throw new JSONException("Inner promotionalOffers array is empty");
        }

        // Получение первого элемента внутреннего массива
        JSONObject offerDetails = promotionalOffersInnerArray.getJSONObject(0).getJSONObject("discountSetting");
        int discount = offerDetails.getInt("discountPercentage");

        return discount == 0;
    }

    private static String parseImageUrl(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getString("type").equals("Thumbnail")) {
                return array.getJSONObject(i).getString("url");
            }
        }

        return null;
    }

    private static LocalDateTime parseDateTime(JSONObject object, String attribute) {
        if (!object.has("promotions") || object.isNull("promotions")) return null;

        JSONArray promotionalOffersArray = new JSONArray();

        // Проверка наличия "promotionalOffers" и "upcomingPromotionalOffers"
        if (object.has("promotions")) {
            JSONObject promotions = object.getJSONObject("promotions");
            if (promotions.has("promotionalOffers")) {
                promotionalOffersArray = promotions.getJSONArray("promotionalOffers");
            }
            if (promotionalOffersArray.isEmpty() && promotions.has("upcomingPromotionalOffers")) {
                promotionalOffersArray = promotions.getJSONArray("upcomingPromotionalOffers");
            }
        }
        else return null;


        // Проверка, что массив содержит элементы
        if (promotionalOffersArray.isEmpty()) {
            throw new JSONException("No promotional offers found");
        }

        // Получение первого элемента "promotionalOffers"
        JSONObject promotionalOffer = promotionalOffersArray.getJSONObject(0);
        JSONArray promotionalOffersInnerArray = promotionalOffer.getJSONArray("promotionalOffers");

        // Проверка, что внутренний массив содержит элементы
        if (promotionalOffersInnerArray.isEmpty()) {
            throw new JSONException("Inner promotionalOffers array is empty");
        }

        // Получение первого элемента внутреннего массива
        JSONObject offerDetails = promotionalOffersInnerArray.getJSONObject(0);

        // Проверка, что элемент содержит нужный атрибут
        if (!offerDetails.has(attribute)) {
            throw new JSONException("JSONObject does not contain attribute '" + attribute + "'");
        }

        // Получение значения атрибута
        String date = offerDetails.getString(attribute);

        // Форматтер для разбора даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Разбор и возврат даты
        return LocalDateTime.parse(date, formatter);
    }

    private static BigDecimal getPrice(JSONObject object) {
        return object.getJSONObject("totalPrice").getBigDecimal("originalPrice")
                .divide(new BigDecimal(100), RoundingMode.HALF_UP);
    }
}
