// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.utils.I2CCOM;
import edu.wpi.first.wpilibj.*;

public class Robot extends TimedRobot {
  I2CCOM arduino;

  double motorSpeed = 0.55; //0.55 lowest speed 1 full speed
  double leftArmMove = 0;
  double rightArmMove = 0;

  DigitalInput rightArmSwitch = new DigitalInput(0);
  DigitalInput leftArmSwitch = new DigitalInput(1);
  //rightArmSwitch.get();

  private final PWMSparkMax m_leftTopMotor = new PWMSparkMax(0);
  private final PWMSparkMax m_leftBottomMotor = new PWMSparkMax(1);
  private final MotorControllerGroup m_left = new MotorControllerGroup(m_leftTopMotor, m_leftBottomMotor);

  private final PWMSparkMax m_rightTopMotor = new PWMSparkMax(2);
  private final PWMSparkMax m_rightBottomMotor = new PWMSparkMax(3);
  private final MotorControllerGroup m_right = new MotorControllerGroup(m_rightTopMotor, m_rightBottomMotor);

  private final PWMSparkMax m_leftArmMotor = new PWMSparkMax(7);
  private final PWMSparkMax m_rightArmMotor = new PWMSparkMax(8);
  
  private boolean switched = false;

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);
  private final Joystick m_stick = new Joystick(0);

  @Override
  public void robotInit() {
    //inverts right side of robot otherwise would be driving in circles when told to go forward
    m_right.setInverted(true);
    //arduino = new I2CCOM(1);
    
  }

  @Override
  public void teleopInit() {
  //ran when teleop is enabled
  /*
  System.out.println("Going Up");
  m_leftArmMotor.set(0.8);
  delay(1000);
  m_leftArmMotor.set(0);
  delay(500);
  System.out.println("Coming Down");
  m_leftArmMotor.set(-0.8);
  delay(750);
  m_leftArmMotor.set(0);
  delay(500);
  */
  }

  @Override
  public void teleopPeriodic() {
    /*
    if(m_stick.getRawButtonPressed(1)) {
      arduino.sendData(1, 1);
    } else {
      arduino.sendData(1, 0);
    }
    */

    double turn = 0;

    if (m_stick.getRawButtonPressed(2)) {
      if(switched) {
        switched = false;
      } else {
        switched = true;
      }
    }

    if (switched == true) {
      turn = m_stick.getZ();
      
    } else if(switched == false) {
      turn = m_stick.getX();
      
    }
    

    double speedPot = m_stick.getThrottle();
    motorSpeed = map(speedPot, 1, -1, 0.55, 1);

    // motor values range from -1 to 1

    if(m_stick.getRawButtonPressed(7)){
        System.out.println("Going Up");
        m_leftArmMotor.set(1);
        m_rightArmMotor.set(1);
        delay(650);
        m_leftArmMotor.set(0);
        m_rightArmMotor.set(0);
    }

    if(m_stick.getRawButtonPressed(8)) {
      System.out.println("Coming Down");
      m_leftArmMotor.set(-1);
      m_rightArmMotor.set(-1);
      delay(525);
      m_leftArmMotor.set(0);
      m_rightArmMotor.set(0);
    }

    m_robotDrive.arcadeDrive(m_stick.getY() * motorSpeed, turn * motorSpeed);
  }

  public double map(double x, double in_min, double in_max, double out_min, double out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }

  public static void delay(int millis){
    try{
      Thread.sleep(millis);
    }catch(Exception E){
    }
  }

}