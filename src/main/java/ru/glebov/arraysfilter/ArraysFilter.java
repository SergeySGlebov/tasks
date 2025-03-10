package ru.glebov.arraysfilter;

import java.util.Arrays;

public class ArraysFilter {

    public static void main(String[] args) {

        String[] strings = new String[]{"One", "Two", "Three"};
        String[] filtered = filter(strings, str -> {
            System.out.println(str);
            return str;
        });
        System.out.println(Arrays.toString(filtered));

    }

    public static <T> T[] filter(T[] array, Filter filter) {
        T[] newArray = Arrays.copyOf(array, array.length);
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = (T) filter.apply(newArray[i]);
        }
        return newArray;
    }

    public interface Filter {
        Object apply(Object o);
    }
}
