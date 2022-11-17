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
    private AddressableLEDBuffer target;
    public LEDEffect[] effects;
    public LEDEffect[] treeEffects;

    public LEDBuffers(AddressableLEDBuffer buffer, int start, int end) {
      target = buffer;
      effects = new LEDEffect[] {
        new Rainbow(buffer, start, end),
        new Gradient(buffer, new Color[] {
          Color.kGreen, Color.kRed, Color.kWhite
        }, 1.0, start, end),

        new Disabled(buffer, start, end),
        new Alternate(buffer, Color.kRed, Color.kGreen, start, end, 10),
        new AlternateTriple(buffer, new Color[] {
          Color.kRed,
          Color.kGreen,
          Color.kWhite
        }, 10, start, end),
        new UkraineLights(buffer, start, end, 1.0)
      };
    }

    public int getBufferLength() {
      return target.getLength();
    }
}
