package ru.glebov.geometryutils;


import ru.glebov.geometrylibrary.Shape;

public class ShapeComparator {
    public static boolean isAreaEqual(Shape first, Shape second) {
        return first.getArea() == second.getArea();
    }

    public static boolean isPerimeterEqual(Shape first, Shape second) {
        return first.getPerimeter() == second.getPerimeter();
    }
    public static void sayHello() {
        System.out.println("Hello");
    }
}
