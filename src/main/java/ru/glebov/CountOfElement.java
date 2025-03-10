package ru.glebov;

public class CountOfElement {
    public static void main(String[] args) {
        String[] array = new String[]{"One", "Two", "Two", "Three", "Three", "Three", null, null};

        System.out.println(Util.countOfElement(array));
    }
}
