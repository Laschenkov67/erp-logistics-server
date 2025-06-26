package com.example.demo.factory;

import com.example.demo.entity.enums.ProductionType;
import java.time.LocalDateTime;
import java.util.Random;

public class TaskFactory {
    private final String[] NAMES = {
            "Отправить посылку #1425",
            "Отправить письмо #1434",
            "Передать пакет #1478",
            "Упаковать письмо #1447",
            "Скомплектовать посылку #1442"
    };
    private final String[] DETAILS = {
            "Добавить бесплатный подарок",
            "Изменить способ доставки на экономичное письмо",
            "Изменить способ доставки на приоритетное письмо",
            "Наклеить пометку: \"Осторожно! Стекло\""
    };

    private final Random random;

    public TaskFactory() {
        random = new Random();
    }

    private String generate(String[] array) {
        return array[random.nextInt(array.length)];
    }

    public String generateName() {
        return generate(NAMES);
    }

    public String generateDetails() {
        return generate(DETAILS);
    }

    public int generateEstimatedTime() {
        return random.nextInt(15) + 5;
    }

    public LocalDateTime generateDeadline() {
        return LocalDateTime.now().plusDays(2);
    }

    public ProductionType generateType() {
        return ProductionType.values()[random.nextInt(ProductionType.values().length)];
    }

    public LocalDateTime generateScheduledTime() {
        return LocalDateTime.now().plusDays(1);
    }
}
