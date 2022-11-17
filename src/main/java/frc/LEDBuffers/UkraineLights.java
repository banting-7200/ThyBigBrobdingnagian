package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.LEDEffect;

public class UkraineLights implements LEDEffect{

    private final Color yellow = new Color(235.0 / 255.0, 235.0 / 255.0, 50.0 / 255.0);
    private final Color blue = Color.kBlue;

    private AddressableLEDBuffer target;
    private double splitPoint;
    private double changeRate;

    private Color a;
    private Color b;

    private int start;
    private int end;

    public UkraineLights(AddressableLEDBuffer target, int start, int end, Double changeRate) {
        this.target = target;
        this.changeRate = changeRate;
        this.start = start;
        this.end = end;

        a = yellow;
        b = blue;
        splitPoint = start;
    }

    @Override
    public AddressableLEDBuffer tick() {
        if(splitPoint >= end) {
            splitPoint = end;
        }

        for(int i = start; i < end; i++) {
            if(i < (int)splitPoint) {
                target.setLED(i, a);
            } else {
                target.setLED(i, b);
            }
        }
        
        if(splitPoint == end) {
            Color t = a;
            a = b;
            b = t;
            
            splitPoint = start;
        }

        splitPoint += changeRate;
        return target;
    }
}
