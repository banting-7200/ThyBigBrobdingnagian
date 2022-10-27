package frc.robot;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

/*
 * This class contains functions that change a private LEDBuffer
 * The LEDBuffer can be applied to an AddressableLED object.
 */
public class LEDBuffers {
    private AddressableLEDBuffer buffer;

    public LEDBuffers(int bufferLength) {
      buffer = new AddressableLEDBuffer(bufferLength);
    }

    public int getBufferLength() {
      return buffer.getLength();
    }

    private int rainbowFirstPixelHue = 0;
    public AddressableLEDBuffer rainbow() {
        for (var i = 0; i < buffer.getLength(); i++) {
          final var hue = (rainbowFirstPixelHue + (i * 180 / buffer.getLength())) % 180;
          buffer.setHSV(i, hue, 255, 128);
        }
    
        rainbowFirstPixelHue += 3;
        rainbowFirstPixelHue %= 180;
        return buffer;
    }
    
    public AddressableLEDBuffer disableLights() {
        for (var i = 0; i < buffer.getLength(); i++) {
          buffer.setHSV(i, 0, 0, 0);
        }

        return buffer;
    }
}
