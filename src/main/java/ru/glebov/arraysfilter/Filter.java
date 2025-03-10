package ru.glebov.arraysfilter;

public interface Filter<T> {
    T apply(T o);
}
