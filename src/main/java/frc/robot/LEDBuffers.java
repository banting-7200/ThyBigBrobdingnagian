package frc.robot;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.LEDBuffers.*;
import frc.robot.utils.LEDEffect;

/*
 * This class contains functions that change a private LEDBuffer
 * The LEDBuffer can be applied to an AddressableLED object.
 */
public class LEDBuffers {
    private AddressableLEDBuffer buffer;
    public LEDEffect[] effects;

    public LEDBuffers(int bufferLength) {
      buffer = new AddressableLEDBuffer(bufferLength);

      effects = new LEDEffect[] {
        new Rainbow(buffer, 0, bufferLength),
        new Gradient(buffer, Color.kGreen, Color.kRed, 10.0, 0, bufferLength),
        new Disabled(buffer, 0, bufferLength),
        new Alternate(buffer, Color.kRed, Color.kGreen, 0, bufferLength, 10),
        new AlternateTriple(buffer, new Color[] {
          Color.kRed,
          Color.kGreen,
          Color.kWhite
        }, 10, 0, bufferLength)
      };
    }

    public int getBufferLength() {
      return buffer.getLength();
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
    public AddressableLEDBuffer knightRiderLight(Color offColor, Color pointerColor, int pDelay, int start, int end, int barWidth) {
      if(knightRiderPosition == -1) {
        knightRiderPosition = (start + end) / 2;
        knightRiderDelta = 1;
      } 

      for(int i = start; i < end; i++) {
        if(i == knightRiderPosition) {
          for(int j = i - barWidth; j < i + barWidth; j++) {
            if(j >= 0 && j < end) {
              buffer.setLED(j, pointerColor);
            }
          }

          i += barWidth;
        } else {
          buffer.setLED(i, offColor);
        }
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

    private int travelBarDelay = 0;
    private int worldBarPosition = 0;
    public AddressableLEDBuffer travelBar(Color offColor, Color pointerColor, int pDelay, int start, int end, int barWidth) {
      if(travelBarDelay <= 0) {
        travelBarDelay = pDelay;
        worldBarPosition++;
      }
      
      for(int i = 0; i < buffer.getLength(); i++) {
        buffer.setLED(i, offColor);
      }

      for(int i = worldBarPosition - barWidth; i <= worldBarPosition + barWidth; i++) {
        int localPosition = (start + i) % (end - start);

        if(localPosition >= start && localPosition < end) {
          buffer.setLED(i, pointerColor);
        }
      }

      travelBarDelay--;
      return buffer;
    }
}
