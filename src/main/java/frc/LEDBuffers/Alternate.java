package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.LEDEffect;

/*
 * This class creates an alternating light effect amongst 2 colours
 */
public class Alternate implements LEDEffect{
    private AddressableLEDBuffer target;
    private boolean alternateAltColors;

    private int alternateDelay;
    private int start;
    private int end;

    private int delay;
    private Color a;
    private Color b;

    public Alternate(AddressableLEDBuffer target, Color a, Color b, int start, int end, int pDelay) {
        this.target = target;
        this.a = a;
        this.b = b;

        this.start = start;
        this.end = end;
        delay = pDelay;
    }

    @Override
    public AddressableLEDBuffer tick() {
        if(alternateDelay <= 0) {
            alternateAltColors = !alternateAltColors;
            alternateDelay = delay;
        }
    
        if(alternateDelay > 0) {
            alternateDelay--;
        }
    
        /*
         * For every 2nd light (i % 2), set LED to a, if alternateAltColors = true, set LED to b
         * For every odd light(i % 2 != 0) set LED to b, if alternateAltColors = true, set LED to a
         */
        for(int i = start; i < end; i++) {
            if(i % 2 == 0) {
              if(alternateAltColors) target.setLED(i, a);
              else target.setLED(i, b);
            } else {
              if(alternateAltColors) target.setLED(i, b);
              else target.setLED(i, a);
            }
        }

        return target;
    }
}
