package frc.robot;

import frc.robot.utils.*;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

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

    private boolean alternateAlternateColors = false;
    private int alternateDelay = 0;
    public void alternate(Color c1, Color c2, int start, int end, int pDelay) {
      if(end >= buffer.getLength() || end < start) {
        System.out.println("endIndex given is out of range");
        return;
      }

      if(start >= buffer.getLength() || start > end) {
        System.out.println("startIndex given is out of range");
        return;
      }

      if(alternateDelay > 0) {
        alternateDelay--;
      }

      if(alternateDelay <= 0) {
        alternateAlternateColors = !alternateAlternateColors;
        alternateDelay = pDelay;
      }

      for(int i = start; i < end; i++) {
        if(i % 2 == 0) {
          if(alternateAlternateColors) buffer.setLED(i, c2);
          if(!alternateAlternateColors) buffer.setLED(i, c1);
        } else {
          if(alternateAlternateColors) buffer.setLED(i, c1);
          if(!alternateAlternateColors) buffer.setLED(i, c2);
        }
      }
    }
    
    public void gradient(double hue1, double hue2, int start, int end) {
      if(end >= buffer.getLength() || end < start) {
        System.out.println("endIndex given is out of range");
        return;
      }

      if(start >= buffer.getLength() || start > end) {
        System.out.println("startIndex given is out of range");
        return;
      }

      for(int i = start; i < end; i++) {
        double percentage = i / (buffer.getLength() * 1.0);
        double hueToUse = Utility.lerp(hue1, hue2, percentage);
        buffer.setHSV(i, (int)hueToUse, 255, 128);
      }
    }

    public void disableLights() {
        AddressableLEDBuffer buffer = new AddressableLEDBuffer(56);
        for (var i = 0; i < buffer.getLength(); i++) {
          buffer.setHSV(i, 0, 0, 0);
        }
    }
}
