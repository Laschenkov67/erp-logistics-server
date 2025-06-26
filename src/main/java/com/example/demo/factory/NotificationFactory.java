package com.example.demo.factory;

import java.util.Random;

public class NotificationFactory {

    private String[] INSTRUCTIONS = {"Повреждённая посылка", "Неоплаченный заказ",
            "Отсутствует один предмет"};

    private String[] DESCRIPTIONS = {"Позвонить получателю", "Выполнить в первую очередь",
            "Передать замечания руководителю"};

    private final Random random;

    private String generate(String[] array) {
        return array[random.nextInt(array.length)];
    }

    public NotificationFactory() {
        random = new Random();
    }

    public String generateInstruction() {
        return generate(INSTRUCTIONS);
    }

    public String generateDescription() {
        return generate(DESCRIPTIONS);
    }
}
