package ru.glebov.arraysfilter;

import java.util.Arrays;

public class ArraysFilter {

    public static void main(String[] args) {

        String[] strings = new String[]{"One", "Two", "Three", null};
        String[] filtered = ArrayUtils.filter(strings, str -> {
            if (str == null) return null;
            return str + " filtered";
        });
        System.out.println(Arrays.toString(filtered));

    }

}
