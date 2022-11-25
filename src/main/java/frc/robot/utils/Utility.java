package frc.robot.utils;

import edu.wpi.first.wpilibj.util.Color;

/*
 * This class contains some commonly used functions/constants across the project
 */
public class Utility {

    /*
     * These integers store the indices of specific lighting effects
     * declared in LEDBuffers.java
     * 
     * Maximum of 6 effects can be declared due to button limits on logitech joystick.
     */
    public static final int FX_RAINBOW = 0;
    public static final int FX_GRADIENT = 1;
    public static final int FX_DISABLED = 2;
    public static final int FX_ALTERNATE = 3;
    public static final int FX_TRIPLEALTERNATE = 4;
    public static final int FX_UKRAINEGRADIENT = 5;
    
    public static final int BASE_LED_COUNT = 56;
    public static final int LED_COUNT = 366;
    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        double ratio = (out_max - out_min) / (in_max - in_min);
        return (x - in_min) * ratio + out_min;
    }

    public static void delay(int millis) {
        try{ Thread.sleep(millis); } catch(Exception e) { }
    }

    public static double clamp(double x, double min, double max) {
        if(x >= max) {
            return max;
        }

        if(x <= min) {
            return min;
        }

        return x;
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
