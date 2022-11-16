package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.LEDEffect;
import frc.robot.utils.Utility;

public class Gradient implements LEDEffect{


    private AddressableLEDBuffer target;
    private double multiplier;
    private double x;
    private Color a;
    private Color b;

    public Gradient(AddressableLEDBuffer target, Color a, Color b, double fadeSpeed, int start, int end) {
        this.target = target;
        this.a = a;
        this.b = b;
        multiplier = 1;
    }

    @Override
    public AddressableLEDBuffer tick() {
        x += 0.02 * multiplier;

        if(x >= 1) {
            x = 1;
            multiplier = -1;
        }

        if(x <= 0) {
            x = 0;
            multiplier = 1;
        }

        Color result = Utility.lerpColors(a, b, x);
        for(int i = 0; i < target.getLength(); i++) {
            target.setLED(i, result);
        }

        return target;
    }
}
