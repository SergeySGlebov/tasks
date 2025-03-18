package ru.glebov.geometryapp;

import ru.glebov.geometrylibrary.Circle;
import ru.glebov.geometrylibrary.Rectangle;
import ru.glebov.geometrylibrary.Triangle;
import ru.glebov.geometrylibrary.threedimensionalshapes.Cube;
import ru.glebov.geometrylibrary.threedimensionalshapes.Sphere;
import ru.glebov.geometryutils.ShapeComparator;
import ru.glebov.geometryutils.UnitConverter;

public class GeometryApp {
    public static void main(String[] args) {
        Circle circle = new Circle(5);
        Rectangle rectangle = new Rectangle(4, 6);
        Triangle triangle = new Triangle(3, 4, 5);

        System.out.println("Circle:");
        System.out.println("Area: " + circle.getArea());
        System.out.println("Perimeter: " + circle.getPerimeter());

        System.out.println("\nRectangle:");
        System.out.println("Area: " + rectangle.getArea());
        System.out.println("Perimeter: " + rectangle.getPerimeter());

        System.out.println("\nTriangle:");
        System.out.println("Area: " + triangle.getArea());
        System.out.println("Perimeter: " + triangle.getPerimeter());
        triangle.sayHello();

        System.out.println("\nПеревод единиц:");
        System.out.println("5 метров в сантиметрах: " + UnitConverter.metersToCentimeters(5));

        System.out.println("\nСравнение фигур:");
        System.out.println("Равны ли площади круга и прямоугольника? " + ShapeComparator.isAreaEqual(circle, rectangle));
        System.out.println("У треугольника и прямоугольника периметр одинаковый? " + ShapeComparator.isPerimeterEqual(triangle, rectangle));
        ShapeComparator.sayHello();

        Cube cube = new Cube(3);
        Sphere sphere = new Sphere(4);

        System.out.println("\nCube:");
        System.out.println("Volume: " + cube.getVolume());
        System.out.println("Surface Area: " + cube.getSurfaceArea());

        System.out.println("\nSphere:");
        System.out.println("Volume: " + sphere.getVolume());
        System.out.println("Surface Area: " + sphere.getSurfaceArea());

    }
}
