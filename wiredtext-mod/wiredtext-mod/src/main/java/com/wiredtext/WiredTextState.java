package com.wiredtext;

import java.util.Random;

public class WiredTextState {

    public static boolean active = false;
    public static long nextTriggerTime = 0;
    public static long showUntil = 0;
    public static String currentText = "";
    public static float textX = 0.5f;
    public static float textY = 0.5f;

    private static final Random RANDOM = new Random();

    // display duration in ms
    public static final long DISPLAY_DURATION_MS = 400;

    // dreamcore / creepy phrases
    private static final String[] PHRASES = {
        "что я тут делаю",
        "где я",
        "я сплю?",
        "это не настоящее",
        "я уже здесь был",
        "выйди отсюда",
        "ты не один",
        "смотри за собой",
        "оглянись",
        "он за тобой",
        "я тебя вижу",
        "ты забыл что-то важное",
        "это всё сон",
        "просыпайся",
        "ты умер во сне",
        "это не твой мир",
        "кто ты такой",
        "я тебя знаю",
        "беги",
        "не смотри вниз",
        "тебя здесь нет",
        "это не твои руки",
        "не открывай глаза",
        "ты давно потерялся",
        "это уже было",
        "помни",
        "забудь",
        "не возвращайся",
        "здесь никого нет",
        "ты один",
        "они уже ушли",
        "где твой дом",
        "ты не помнишь меня",
        "я был здесь первым",
        "не оглядывайся",
        "он знает",
        "тихо",
        "осторожно",
        "не дыши"
    };

    // random-letter gibberish generator
    private static final String CHARS = "АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзиклмнопрстуфхцчшщъыьэюя";

    public static String randomGibberish() {
        int len = 4 + RANDOM.nextInt(10);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (RANDOM.nextFloat() < 0.15f) sb.append(' ');
            else sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString().trim();
    }

    public static String pickText() {
        // 40% chance gibberish, 60% chance phrase
        if (RANDOM.nextFloat() < 0.40f) {
            return randomGibberish();
        } else {
            return PHRASES[RANDOM.nextInt(PHRASES.length)];
        }
    }

    public static void scheduleNext() {
        // 1 to 6 minutes in milliseconds
        long minMs = 60_000L;
        long maxMs = 360_000L;
        long delay = minMs + (long)(RANDOM.nextDouble() * (maxMs - minMs));
        nextTriggerTime = System.currentTimeMillis() + delay;
    }

    public static void trigger() {
        currentText = pickText();
        // random position: keep away from edges (5%–90% of screen)
        textX = 0.05f + RANDOM.nextFloat() * 0.85f;
        textY = 0.05f + RANDOM.nextFloat() * 0.85f;
        showUntil = System.currentTimeMillis() + DISPLAY_DURATION_MS;
        scheduleNext();
    }

    public static boolean isShowing() {
        return active && System.currentTimeMillis() < showUntil;
    }

    public static void tick() {
        if (!active) return;
        long now = System.currentTimeMillis();
        if (nextTriggerTime > 0 && now >= nextTriggerTime) {
            trigger();
        }
    }
}
