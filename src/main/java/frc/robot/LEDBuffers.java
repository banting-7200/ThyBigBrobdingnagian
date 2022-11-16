package frc.robot;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.LEDBuffers.*;
import frc.robot.utils.LEDEffect;

/*
 * This class contains different classes that contain different effects.
 * The LEDBuffer can be applied to an AddressableLED object.
 * 
 * The corresponding array indices to each effect can be found in Utility.java
 */
public class LEDBuffers {
    private AddressableLEDBuffer buffer;
    public LEDEffect[] effects;

    public LEDBuffers(int bufferLength) {
      buffer = new AddressableLEDBuffer(bufferLength);

      effects = new LEDEffect[] {
        new Rainbow(buffer, 0, bufferLength),
        new Gradient(buffer, new Color[] {
          Color.kGreen, Color.kRed, Color.kWhite
        }, 1.0, 0, bufferLength),

        new Disabled(buffer, 0, bufferLength),
        new Alternate(buffer, Color.kRed, Color.kGreen, 0, bufferLength, 10),
        new AlternateTriple(buffer, new Color[] {
          Color.kRed,
          Color.kGreen,
          Color.kWhite
        }, 10, 0, bufferLength),
        new Gradient(buffer, new Color[] {
          new Color(235.0 /255.0, 50.0 / 255.0, 235.0 / 255.0), new Color(0.0, 1.0, 0)
        }, 0.5, 0, bufferLength),
      };
    }

    public int getBufferLength() {
      return buffer.getLength();
    }
}
