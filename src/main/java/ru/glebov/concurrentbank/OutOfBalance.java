package ru.glebov.concurrentbank;

public class OutOfBalance extends RuntimeException {
    public OutOfBalance(String message) {
        super(message);
    }
}
