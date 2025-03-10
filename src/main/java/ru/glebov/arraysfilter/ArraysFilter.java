package ru.glebov.arraysfilter;

import java.util.Arrays;

public class ArraysFilter {

    public static void main(String[] args) {

        String[] strings = new String[]{"One", "Two", "Three", null};
        String[] filtered = filter(strings, str -> {
            if(str == null) return null;
            return str + " filtered";
        });
        System.out.println(Arrays.toString(filtered));

    }

    public static <T> T[] filter(T[] array, Filter<T> filter) {
        T[] newArray = Arrays.copyOf(array, array.length);
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = filter.apply(newArray[i]);
        }
        return newArray;
    }

    public interface Filter<T> {
        T apply(T o);
    }
}
