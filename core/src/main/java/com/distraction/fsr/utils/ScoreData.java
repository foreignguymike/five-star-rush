package com.distraction.fsr.utils;

import com.distraction.fsr.entity.Food;

import java.util.HashMap;
import java.util.Map;

public class ScoreData {

    public Map<Food.Type, Integer> map = new HashMap<>();
    public int bonusCount;

    public void addFood(Food.Type type) {
        boolean exists = false;
        for (Map.Entry<Food.Type, Integer> entry : map.entrySet()) {
            if (entry.getKey() == type) {
                entry.setValue(entry.getValue() + 1);
                exists = true;
                break;
            }
        }
        if (!exists) {
            map.put(type, 1);
        }
    }

    public void addBonus() {
        bonusCount++;
    }

}
