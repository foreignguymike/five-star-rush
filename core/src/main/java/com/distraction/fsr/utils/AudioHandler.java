package com.distraction.fsr.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class AudioHandler {

    private final Map<String, Sound> sounds;

    public AudioHandler() {

        sounds = new HashMap<>();
        addSound("clickup", "sfx/clickup.wav");
        addSound("clickdown", "sfx/clickdown.wav");
        addSound("foodjump", "sfx/foodjump.wav");
        addSound("bonus", "sfx/bonus.wav");
        addSound("bonusfail", "sfx/bonusfail.wav");
        addSound("countdownlow", "sfx/countdownlow.wav");
        addSound("countdownhigh", "sfx/countdownhigh.wav");
    }

    private void addSound(String key, String fileName) {
        sounds.put(key, Gdx.audio.newSound(Gdx.files.internal(fileName)));
    }

    public void playSound(String key) {
        playSound(key, 1);
    }

    public void playSound(String key, float volume) {
        for (Map.Entry<String, Sound> entry : sounds.entrySet()) {
            if (entry.getKey().equals(key)) entry.getValue().play(volume);
        }
    }

}
