package ru.glebov.geometrylibrary.threedimensionalshapes;

public class Cube extends DimensionalShapes{
    private final double side;

    public Cube(double side) {
        if (side <= 0) {
            throw new IllegalArgumentException("Сторона должна быть положительной");
        }
        this.side = side;
    }

    @Override
    public double getVolume() {
        return Math.pow(side, 3);
    }

    @Override
    public double getSurfaceArea() {
        return 6 * Math.pow(side, 2);
    }
}
