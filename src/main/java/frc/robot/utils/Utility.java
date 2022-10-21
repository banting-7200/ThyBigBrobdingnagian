package frc.robot.utils;

public class Utility {
    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        double ratio = (out_max - out_min) / (in_max - in_min);
        return (x - in_min) * ratio + out_min;
    }
}
