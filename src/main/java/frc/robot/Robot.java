// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class Robot extends TimedRobot {
  double motorSpeed = 0.55; //0.55 lowest speed 1 full speed

  private final PWMSparkMax m_leftTopMotor = new PWMSparkMax(0);
  private final PWMSparkMax m_leftBottomMotor = new PWMSparkMax(1);
  private final MotorControllerGroup m_left = new MotorControllerGroup(m_leftTopMotor, m_leftBottomMotor);

  private final PWMSparkMax m_rightTopMotor = new PWMSparkMax(2);
  private final PWMSparkMax m_rightBottomMotor = new PWMSparkMax(3);
  private final MotorControllerGroup m_right = new MotorControllerGroup(m_rightTopMotor, m_rightBottomMotor);

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);
  private final Joystick m_stick = new Joystick(0);

  @Override
  public void robotInit() {
    //inverts right side of robot otherwise would be driving in circles when told to go forward

    m_right.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    
    double turn;

    if(m_stick.getRawButton(2)) { //Allows steering toggle
      turn = m_stick.getZ();
    } else {
      turn = m_stick.getX();
    }

    System.out.println(m_stick.getRawButton(2));

    double speedPot = m_stick.getThrottle();
    motorSpeed = map(speedPot, 1, -1, 0.55, 1);
    

    m_robotDrive.arcadeDrive(m_stick.getY() * motorSpeed, turn * motorSpeed);
  }

  public double map(double x, double in_min, double in_max, double out_min, double out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }
}