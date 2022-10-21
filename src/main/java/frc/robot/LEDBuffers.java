package frc.robot;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class LEDBuffers {
    private int rainbowFirstPixelHue = 0;
    public AddressableLEDBuffer buffer;

    public LEDBuffers(int bufferLength) {
      buffer = new AddressableLEDBuffer(bufferLength);
    }

    public void rainbow() {
        AddressableLEDBuffer buffer = new AddressableLEDBuffer(56);
    
        for (var i = 0; i < buffer.getLength(); i++) {
          final var hue = (rainbowFirstPixelHue + (i * 180 / buffer.getLength())) % 180;
          buffer.setHSV(i, hue, 255, 128);
        }
    
        rainbowFirstPixelHue += 3;
        rainbowFirstPixelHue %= 180;
    }
    
    public void disableLights() {
        AddressableLEDBuffer buffer = new AddressableLEDBuffer(56);
        for (var i = 0; i < buffer.getLength(); i++) {
          buffer.setHSV(i, 0, 0, 0);
        }
    }
}
