package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.LEDEffect;
import frc.robot.utils.Utility;

/*
 * This class fades between colors based on a colour array.
 */
public class Gradient implements LEDEffect{
    private AddressableLEDBuffer target; // The LEDBuffer to affect
    private double fadeSpeed; // The speed at which the effect operates
    private double x; // This variables stores the time interpolation between two colours

    private Color[] colors; // Colours to fade through
    private int prevIndex; // Stores index of colour a in fade operation
    private int currIndex; // Stores index of colour b in fade operation

    private int start; // The starting index at which the effect will affect
    private int end; // The end index at which the effect will affect.

    public Gradient(AddressableLEDBuffer target, Color[] colors, double fadeSpeed, int start, int end) {
        this.target = target;
        this.colors = colors;

        this.fadeSpeed = fadeSpeed;
        this.start = start;
        this.end = end;

        prevIndex = 0;
        currIndex = 1;
    }

    @Override
    public AddressableLEDBuffer tick() {
        x += 0.02 * fadeSpeed;

        // Linearly interpolate between colours a/b and apply resultant colour to LEDs.
        Color result = Utility.lerpColors(colors[prevIndex], colors[currIndex], x);
        for(int i = start; i < end; i++) {
            target.setLED(i, result);
        }

        /*
         * If the colour time has reached the b colour value,
         *  - reset time and increase previous colour index and current colour index
         *  - reset prev/curr colour indices if they go out of colour array bounds.
         */
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
