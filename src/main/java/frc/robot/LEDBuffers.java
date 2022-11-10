package frc.robot;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

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
    public AddressableLEDBuffer rainbow(int start, int end) {
      for (var i = start; i < end; i++) {
        final var hue = (rainbowFirstPixelHue + (i * 180 / buffer.getLength())) % 180;
        buffer.setHSV(i, hue, 255, 128);
      }
    
      rainbowFirstPixelHue += 3;
      rainbowFirstPixelHue %= 180;
      return buffer;
    }

    private int alternateDelay = 0;
    private boolean alternateAltColors;
    public AddressableLEDBuffer alternate(Color a, Color b, int pDelay, int start, int end) {
      if(alternateDelay <= 0) {
        alternateAltColors = !alternateAltColors;
        alternateDelay = pDelay;
      }

      if(alternateDelay > 0) {
        alternateDelay--;
      }

      for(int i = start; i < end; i++) {
        if(i % 2 == 0) {
          if(alternateAltColors) buffer.setLED(i, a);
          else buffer.setLED(i, b);
        } else {
          if(alternateAltColors) buffer.setLED(i, b);
          else buffer.setLED(i, a);
        }
      }

      return buffer;
    }
    
    private int knightRiderDelayBeforeTick;
    private int knightRiderPosition = -1;
    private int knightRiderDelta;
    public AddressableLEDBuffer knightRiderLight(Color offColor, Color pointerColor, int pDelay, int start, int end) {

      if(knightRiderPosition == -1) {
        knightRiderPosition = (start + end) / 2;
        knightRiderDelta = 1;
      } 

      for(int i = start; i < end; i++) {
        if(i == knightRiderPosition) {
          buffer.setLED(i, pointerColor);
          continue;
        }

        buffer.setLED(i, offColor);
      }

      if(knightRiderDelayBeforeTick > 0) {
        knightRiderDelayBeforeTick--;
      } else {
        if(knightRiderPosition == end) {
          knightRiderDelta = -1;
        } else if(knightRiderPosition == start) {
          knightRiderDelta = 1;
        }
  
        knightRiderPosition += knightRiderDelta;
        knightRiderDelayBeforeTick = pDelay;
      }

      return buffer;
    }

    public AddressableLEDBuffer disableLights(int start, int end) {
        if(start < 0 && start >= buffer.getLength() && start > end) return buffer;
        if(end < 0 && end >= buffer.getLength() && end < start) return buffer;

        for (var i = 0; i < buffer.getLength(); i++) {
          buffer.setHSV(i, 0, 0, 0);
        }

        return buffer;
    }
}
