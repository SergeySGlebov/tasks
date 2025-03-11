package ru.glebov;

import java.util.HashMap;
import java.util.Map;

public class Util {
    public  static <T> Map<T, Integer> countOfElement(T[] array) {
        Map<T, Integer> result = new HashMap<>();
        for (T t : array) {
            result.merge(t, 1, (prev, one) -> prev + 1);
 //           result.put(t, result.getOrDefault(t, 0) + 1);
        }
        return result;
    }
}
