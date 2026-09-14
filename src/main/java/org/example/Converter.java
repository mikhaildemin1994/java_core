package org.example;

import java.util.HashMap;
import java.util.Map;

public class Converter {
    public <T> Map<T, Integer> convertArrayToMap(T[] array) {
        int count = 1;
        Map<T, Integer> convertedMap = new HashMap<>();
        for (T a : array) {
            if (convertedMap.containsKey(a)) {
                convertedMap.put(a, convertedMap.getOrDefault(a, 0) + count);
            } else {
                convertedMap.put(a, count);
            }
        }
        return convertedMap;
    }
}
