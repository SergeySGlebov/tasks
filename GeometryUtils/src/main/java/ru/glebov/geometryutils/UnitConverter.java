package ru.glebov.geometryutils;

public class UnitConverter {
    public static double metersToCentimeters(double meters) {
        return meters * 100;
    }

    public static double centimetersToMeters(double centimeters) {
        return centimeters / 100;
    }
}
