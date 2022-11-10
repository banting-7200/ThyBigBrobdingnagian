package frc.robot.utils;

import edu.wpi.first.wpilibj.util.Color;

/*
 * This class contains some commonly used functions across the project
 */
public class Utility {
    public static final int LED_COUNT = 306;

    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        double ratio = (out_max - out_min) / (in_max - in_min);
        return (x - in_min) * ratio + out_min;
    }

    public static void delay(int millis){
        try{ Thread.sleep(millis); } catch(Exception e) { }
    }

    // Linearly interpolate between two values with 
    // a given percentage (t-value) (0-1)
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public static Color lerpColors(Color ac, Color bc, double t) {
        double r = lerp(ac.red, bc.red, t);
        double g = lerp(ac.green, bc.green, t);
        double b = lerp(ac.blue, bc.blue, t);
        return new Color(r, g, b);
    }
}
