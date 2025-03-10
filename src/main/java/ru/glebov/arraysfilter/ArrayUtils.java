package ru.glebov.arraysfilter;

import java.util.Arrays;

public class ArrayUtils {

    public static <T> T[] filter(T[] array, Filter<T> filter) {
        T[] newArray = Arrays.copyOf(array, array.length);
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = filter.apply(newArray[i]);
        }
        return newArray;
    }

}
