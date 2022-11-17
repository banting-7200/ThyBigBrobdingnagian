package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.robot.utils.LEDEffect;

public class Rainbow implements LEDEffect{

    private AddressableLEDBuffer buffer;
    private int rainbowFirstPixelHue;

    private final int start;
    private final int end;

    public Rainbow(AddressableLEDBuffer targetBuffer, int start, int end) {
        buffer = targetBuffer;
        this.start = start;
        this.end = end;

        rainbowFirstPixelHue = 0;
    }

    @Override
    public AddressableLEDBuffer tick() {
        for (var i = start; i < end; i++) {
            int hue = (rainbowFirstPixelHue + (i * 180 / buffer.getLength())) % 180;
            buffer.setHSV(i, hue, 255, 128);
        }
        
        rainbowFirstPixelHue += 3;
        rainbowFirstPixelHue %= 180;
        return buffer;
    }
}
