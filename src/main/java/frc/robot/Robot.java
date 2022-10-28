// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.utils.I2CCOM;
import frc.robot.utils.Utility;
import edu.wpi.first.wpilibj.*;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {
  I2CCOM arduino;

  public int m_rainbowFirstPixelHue = 0;

  double motorSpeed = 0.6;//0.55 lowest speed 1 full speed
  double leftArmMove = 0;
  double rightArmMove = 0;

  DigitalInput leftArmSwitch = new DigitalInput(0);
  DigitalInput rightArmSwitch = new DigitalInput(1);

  private final PWMSparkMax m_leftTopMotor = new PWMSparkMax(0);
  private final PWMSparkMax m_leftBottomMotor = new PWMSparkMax(1);
  private final MotorControllerGroup m_left = new MotorControllerGroup(m_leftTopMotor, m_leftBottomMotor);

  private final PWMSparkMax m_rightTopMotor = new PWMSparkMax(2);
  private final PWMSparkMax m_rightBottomMotor = new PWMSparkMax(3);
  private final MotorControllerGroup m_right = new MotorControllerGroup(m_rightTopMotor, m_rightBottomMotor);

  private final PWMSparkMax m_leftArmMotor = new PWMSparkMax(8);
  private final PWMSparkMax m_rightArmMotor = new PWMSparkMax(7);

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);
  private final Joystick m_stick = new Joystick(0);

  //private boolean switched = false;
  private boolean rainbowSwitched = false;
  public boolean rightArmToggleBool = false;
  public boolean leftArmToggleBool = false;

  AddressableLED m_led = new AddressableLED(9);
  LEDBuffers bufferCreator = new LEDBuffers(60);

  DoubleSolenoid head = new DoubleSolenoid(9, PneumaticsModuleType.CTREPCM, 1, 0);

  // Button Map
  int headToggle = 1;
  //int invertDrive = 2;
  int rainbowLightToggle = 2;
  int leftArmToggleButton = 5;
  int rightArmToggleButton = 6;
  int leftArmFullButton = 7;
  int rightArmFullButton = 8;
  int leftArmHalfButton = 9;
  int rightArmHalfButton = 10;
  int leftForceLimitButton = 11;
  int rightForceLimitButton = 12;
  
  @Override
  public void robotInit() {
    m_right.setInverted(true);

    m_led.setLength(bufferCreator.buffer.getLength());
    m_led.start();

    head.set(kReverse);

    CameraServer.startAutomaticCapture();
    
  }

  @Override
  public void robotPeriodic() {
    if(m_stick.getRawButtonPressed(rainbowLightToggle)){
      rainbowSwitched = !rainbowSwitched;
    }

    if (rainbowSwitched == true) {
      bufferCreator.rainbow();
    } else if(rainbowSwitched == false) {
      bufferCreator.disableLights();
    }

    m_led.setData(bufferCreator.buffer);
  }

  @Override
  public void teleopInit() {
  //ran when teleop is enabled
  }

  @Override
  public void teleopPeriodic() {
    //System.out.println("Right Limit Switch held: " + rightArmSwitch.get() + ". Left Switch Held: " + leftArmSwitch.get());
    
    double turn = m_stick.getX();

    /*
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
    */

    double armSpeedTemp = m_stick.getThrottle();
    double armSpeed = map(armSpeedTemp, 1, -1, 0.4, 1);

    Thread leftArm = new Thread() {
      public void run() {
        while(leftArmSwitch.get() == false && m_stick.getRawButtonPressed(leftForceLimitButton) == false) {
            m_leftArmMotor.set(armSpeed); //was 1
        }
        m_leftArmMotor.set(0);
        delay(200);
        m_leftArmMotor.set(-1);
        delay(475); //550
        m_leftArmMotor.set(0);
        leftArmToggleBool = false;
      }
    };

    Thread rightArm = new Thread() {
      public void run() {
        while(rightArmSwitch.get() == false && m_stick.getRawButtonPressed(rightForceLimitButton) == false) {
          m_rightArmMotor.set(armSpeed);
        }
        m_rightArmMotor.set(0);
        delay(200);
        m_rightArmMotor.set(-1);
        delay(475); // 550
        m_rightArmMotor.set(0);
        rightArmToggleBool = false;
      }
    };

    Thread leftHalf = new Thread() { //elbow
      public void run() {
        if(leftArmSwitch.get() == false) {
          m_leftArmMotor.set(1);
          delay(280); //350
          // fail safe
          if(leftArmSwitch.get() == true) {
            m_leftArmMotor.set(-1);
            delay(475); //550
            m_leftArmMotor.set(0);
          }
          m_leftArmMotor.set(0);
          delay(250);
          m_leftArmMotor.set(-1);
          delay(250);
          m_leftArmMotor.set(0); 
        }
      }
    };

    Thread rightHalf = new Thread() {
      public void run() {
        if(rightArmSwitch.get() == false) {
          m_rightArmMotor.set(1);
          delay(280); //350
          // fail safe
          if(rightArmSwitch.get() == true) {
            m_rightArmMotor.set(-1);
            delay(475); //550
            m_rightArmMotor.set(0);
          }
          m_rightArmMotor.set(0);
          delay(250);
          m_rightArmMotor.set(-1);
          delay(250);
          m_rightArmMotor.set(0);

        }
      }
    };

    Thread leftHalfToggle = new Thread() {
      public void run() {
        if(leftArmSwitch.get() == false) {
          leftArmToggleBool = true;

          m_leftArmMotor.set(0.7);
          delay(350); //350

          // fail safe
          if(leftArmSwitch.get() == true) {
            m_leftArmMotor.set(-1);
            delay(475); //550
            m_leftArmMotor.set(0);
            leftArmToggleBool = false;
            return; // end thread
          }
          m_leftArmMotor.set(0);

          while(!m_stick.getRawButtonPressed(leftArmToggleButton)) {}

          delay(250);
          m_leftArmMotor.set(-0.7);
          delay(280);
          m_leftArmMotor.set(0);

          leftArmToggleBool = false;
        }
      }
    };

    Thread rightHalfToggle = new Thread() {
      public void run() {
        if(rightArmSwitch.get() == false) {
          rightArmToggleBool = true;

          m_rightArmMotor.set(0.7);
          delay(350); //350

          // fail safe
          if(rightArmSwitch.get() == true) {
            m_rightArmMotor.set(-1);
            delay(475); //550
            m_rightArmMotor.set(0);
            rightArmToggleBool = false;
            return; // End Thread
          }
          m_rightArmMotor.set(0);

          while(!m_stick.getRawButtonPressed(rightArmToggleButton)) {}

          delay(250);
          m_rightArmMotor.set(-0.7);
          delay(280);
          m_rightArmMotor.set(0);

          rightArmToggleBool = false;
        }
      }
    };

    if(m_stick.getRawButtonPressed(leftArmToggleButton)) {
      if(leftArmToggleBool == false){
        leftHalfToggle.start();
      }
    }

    if(m_stick.getRawButtonPressed(rightArmToggleButton)) {
      if(rightArmToggleBool == false) {
        rightHalfToggle.start();
      }
    }

    if(m_stick.getRawButtonPressed(leftArmFullButton)) {
      //leftArm.start();      shoulder movements,  not needed for wings so temp taken out
    }

    if(m_stick.getRawButtonPressed(rightArmFullButton)){
      System.out.println("Arm Button Pressed");
      //rightArm.start();
    }

    if(m_stick.getRawButtonPressed(leftArmHalfButton)) {
      leftHalf.start();
    }

    if(m_stick.getRawButtonPressed(rightArmHalfButton)) {
      rightHalf.start();
    }

    if(m_stick.getRawButtonPressed(headToggle)) {
      head.toggle();
    }

    m_robotDrive.arcadeDrive(m_stick.getY() * motorSpeed, turn * motorSpeed);
  }

  public double map(double x, double in_min, double in_max, double out_min, double out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }

  public void delay(int millis){
    try{
      Thread.sleep(millis);
    }catch(Exception E){
    }
  }
}
