package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.LEDEffect;

public class Disabled implements LEDEffect {

    private AddressableLEDBuffer target;
    private final Color clear = new Color(0, 0, 0);
    private int start;
    private int end;
    public Disabled(AddressableLEDBuffer target, int start, int end) {
        this.target = target;
        this.start = start;
        this.end = end;
    }

    @Override
    public AddressableLEDBuffer tick() {
        for(int i = start; i < end; i++) {
            target.setLED(i, clear);
        }

        return target;
    }
    
}
