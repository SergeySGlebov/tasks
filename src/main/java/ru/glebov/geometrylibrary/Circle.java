package ru.glebov.geometrylibrary;

public class Circle extends Shape{
    private final int radius;

    public Circle(int radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Радиус должен быть положительный");
        }
        this.radius = radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }
}
