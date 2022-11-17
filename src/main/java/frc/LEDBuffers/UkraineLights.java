package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.LEDEffect;
import frc.robot.utils.Utility;

/*
 * This class makes lights make a gradient for ukraine
 */
public class UkraineLights implements LEDEffect{

    private final Color yellow = new Color(.8, 1.0, 0.0);
    private final Color blue = Color.kBlue;

    private AddressableLEDBuffer target;
    private double changeRate;

    private double lerpOffset;
    private int start;
    private int end;

    public UkraineLights(AddressableLEDBuffer target, int start, int end, Double changeRate) {
        this.target = target;
        this.changeRate = changeRate;
        
        this.start = start;
        this.end = end;
    }

    @Override
    public AddressableLEDBuffer tick() {
        for(int i = start; i < end; i++) {
            // Using light index, divide it by LEDnum for percentage of 2pi
            // Use percentage to evaluate f(x) [see above]
            double percentage = ((double)(i - start) / (end - start));
            double piPerc = (Math.PI * 2.0) * percentage;

            // Use resultant of f(x) to lerp between colour a and colour b with applied offset.
            double x = getEvaluationPercentage(piPerc, Math.toRadians(lerpOffset));
            target.setLED(i, Utility.lerpColors(yellow, blue, x));
        }

        lerpOffset += changeRate;
        return target;
    }

    //f(x) = (1/2)cos(x)+(1/2)
    private double getEvaluationPercentage(double x, double offset) {
        return (0.5 * Math.cos(x + offset)) + 0.5;
    }
}
