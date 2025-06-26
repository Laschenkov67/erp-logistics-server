package com.example.demo.factory;

import java.util.Random;

public class SuggestionFactory {

    private final String[] NAMES = {
            "Провести обучение",
            "Организовать встречу",
            "Изменить количество людей"
    };

    private final String[] DESCRIPTIONS = {
            "Инициатива, предложенная несколькими сотрудниками",
            "Подано после согласования с руководителем",
            "Предложено во время конференции"
    };

    private final Random random;

    public SuggestionFactory() {
        random = new Random();
    }

    private String generate(String[] array) {
        return array[random.nextInt(array.length)];
    }

    public String generateName() {
        return generate(NAMES);
    }

    public String generateDescription() {
        return generate(DESCRIPTIONS);
    }
}
