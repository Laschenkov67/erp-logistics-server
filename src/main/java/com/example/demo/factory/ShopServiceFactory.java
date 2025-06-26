package com.example.demo.factory;

import com.example.demo.entity.enums.Resolution;

import java.util.Random;

public class ShopServiceFactory {

    private final String[] FIRST_NAMES = {"Ольга", "Иван", "Дмитрий",
            "Алексей", "Екатерина", "Анна", "Юлия", "Сергей",
            "Константин", "Александр", "Мария", "Наталья", "Михаил"};

    private final String[] LAST_NAMES = {"Иванов", "Петров", "Сидоров",
            "Кузнецов", "Смирнов", "Васильев", "Павлов"};

    private final String[] EMAILS = {
            "firma@firma.ru",
            "przedsiebiorstwo@przedsiebiorstwo.ru",
            "business@business.ru",
            "uczelnia@uczelnia.ru",
            "hurtownia@hurtownia.ru"};

    private final String[] PHONE_NUMBERS = {"625875322", "158643422", "567154333", "312312800", "321212890"};

    private final String[] STREETS = {"ул. Ленина", "ул. Пушкина", "ул. Гагарина",
            "ул. Советская", "ул. Мира", "ул. Октябрьская", "ул. Центральная",
            "ул. Садовая", "ул. Молодёжная", "ул. Школьная"};

    private final String[] HOUSE_NUMBERS = {"11/16", "36", "239b", "8e", "16b"};

    private final String[] CITIES = {"Москва", "Санкт-Петербург", "Новосибирск",
            "Екатеринбург", "Казань", "Нижний Новгород", "Краснодар",
            "Воронеж", "Самара", "Ростов-на-Дону"};

    private final String[] POSTAL_CODES = {"96-111", "44-144", "77-140", "11-997", "50-001"};

    private final Random random;

    public ShopServiceFactory() {
        random = new Random();
    }

    private String generate(String[] array) {
        return array[random.nextInt(array.length)];
    }

    public String generateFirstName() {
        return generate(FIRST_NAMES);
    }

    public String generateLastName() {
        return generate(LAST_NAMES);
    }

    public String generateEmail() {
        return generate(EMAILS);
    }

    public String generatePhoneNumber() {
        return generate(PHONE_NUMBERS);
    }

    public String generateStreet() {
        return generate(STREETS);
    }

    public String generateHouseNumber() {
        return generate(HOUSE_NUMBERS);
    }

    public String generateCity() {
        return generate(CITIES);
    }

    public String generatePostalCode() {
        return generate(POSTAL_CODES);
    }

    public Resolution generateResolution() {
        return Resolution.values()[random.nextInt(Resolution.values().length)];
    }
}
