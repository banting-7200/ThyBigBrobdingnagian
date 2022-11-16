package frc.LEDBuffers;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.LEDEffect;

public class AlternateTriple implements LEDEffect{

    private AddressableLEDBuffer target;
    private Color[] colors;

    private int start, end;
    private int offset;
    private int pDelay;
    private int delay;

    public AlternateTriple(AddressableLEDBuffer buffer, Color[] colors, int pDelay, int start, int end) {
        target = buffer;
        this.colors = colors;

        this.pDelay = pDelay;
        delay = pDelay;

        this.start = start;
        this.end = end;
    } //hi ava was here

    @Override
    public AddressableLEDBuffer tick() {
        if(delay <= 0) {
            delay = pDelay;
            offset++;

            if(offset > 2) {
                offset = 0;
            }
        }

        for(int i = start; i < end; i++) {
            if(i % 3 == 0) {
                if(offset == 0) {
                    target.setLED(i, colors[0]);
                } else if(offset == 1) {
                    target.setLED(i, colors[1]);
                } else if(offset == 2) {
                    target.setLED(i, colors[2]);
                }
                continue;
            }

            if(i % 2 == 0) {
                if(offset == 0) {
                    target.setLED(i, colors[1]);
                } else if(offset == 1) {
                    target.setLED(i, colors[2]);
                } else if(offset == 2) {
                    target.setLED(i, colors[0]);
                }
                continue;
            }

            if(offset == 0) {
                target.setLED(i, colors[2]);
            } else if(offset == 1) {
                target.setLED(i, colors[0]);
            } else if(offset == 2) {
                target.setLED(i, colors[1]);
            }
        }

        delay--;
        return target;
    }
}
