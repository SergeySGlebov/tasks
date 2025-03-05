package ru.glebov;

import java.util.Stack;

public class OwnStringBuilder {
    private StringBuilder stringBuilder;
    private final Stack<String> history;

    public OwnStringBuilder() {
        stringBuilder = new StringBuilder();
        history = new Stack<>();
    }

    public OwnStringBuilder append(String str) {
        history.push(stringBuilder.toString());
        stringBuilder.append(str);
        return this;
    }

    public OwnStringBuilder insert(int offset, String str) {
        history.push(stringBuilder.toString());
        stringBuilder.insert(offset, str);
        return this;
    }

    public OwnStringBuilder delete(int start, int end) {
        history.push(stringBuilder.toString());
        stringBuilder.delete(start, end);
        return this;
    }

    public OwnStringBuilder undo() {
        if(!history.isEmpty()) {
            stringBuilder = new StringBuilder(history.pop());
        }
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        OwnStringBuilder osb = new OwnStringBuilder();
        osb.append("Добавляем").append(" и это добавляем");
        System.out.println(osb); // "Добавляем и это добавляем"

        osb.insert(9, " в серединку");
        System.out.println(osb); // "Добавляем в серединку и это добавляем"

        osb.delete(19, 20);
        System.out.println(osb); // "Добавляем в середину и это добавляем" (убрали "к" из "серединку")

        osb.undo();
        System.out.println(osb); // "Добавляем в серединку и это добавляем"

        osb.undo();
        System.out.println(osb); // "Добавляем и это добавляем"

        osb.undo();
        System.out.println(osb); // "Добавляем"

        osb.undo();
        System.out.println(osb);
    }

}
