package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.LEDEffect;
import frc.robot.utils.Utility;

//Color array given MUST HAVE LENGTH > 2!
public class Gradient implements LEDEffect{


    private AddressableLEDBuffer target;
    private double fadeSpeed;
    private double multiplier;
    private double x;

    private Color[] colors;
    private int prevIndex;
    private int currIndex;

    private int start;
    private int end;

    public Gradient(AddressableLEDBuffer target, Color[] colors, double fadeSpeed, int start, int end) {
        this.target = target;
        this.colors = colors;
        this.start = start;
        this.end = end;
        multiplier = 1;

        this.fadeSpeed = fadeSpeed;
        prevIndex = 0;
        currIndex = 1;
    }

    @Override
    public AddressableLEDBuffer tick() {
        x += 0.02 * fadeSpeed * multiplier;

        Color result = Utility.lerpColors(colors[prevIndex], colors[currIndex], x);
        for(int i = start; i < end; i++) {
            target.setLED(i, result);
        }

        if(x >= 1) {
            x = 0.0;
            prevIndex++;
            currIndex++;

            if(currIndex == colors.length) {
                currIndex = 0;
            }

            if(prevIndex == colors.length) {
                prevIndex = 0;
            }
        }

        return target;
    }
}
