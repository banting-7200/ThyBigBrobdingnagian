package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.LEDEffect;
import frc.robot.utils.Utility;

/*
 * This class makes lights make a gradient for ukraine
 * f(x, d) = round(0.5 * Math.cos(x + d) + 0.5)
 */
public class UkraineLights implements LEDEffect{
    private final Color yellow = new Color(.8, 0.9, 0.0);
    private final Color blue = Color.kBlue;

    private AddressableLEDBuffer target; //target LEDBuffer to change
    private double changeRate; // this is the 'd' value in f(x, d)

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

        //Increase by changeRate/periodicCycleTime(ms)
        lerpOffset += changeRate;
        return target;
    }

    // f(x, d) = round(0.5 * Math.cos(x + d) + 0.5)
    private double getEvaluationPercentage(double x, double offset) {
        return Math.round((0.5 * Math.cos(x + offset)) + 0.5);
    }
}
