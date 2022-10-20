package frc.robot;

import edu.wpi.first.wpilibj.*;

public class Input {
    public final Joystick m_Stick;

    public Input(int joyStickPort) {
        m_Stick = new Joystick(0);
    }
}
