// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.utils.I2CCOM;
import edu.wpi.first.wpilibj.*;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import org.opencv.core.Mat;


public class Robot extends TimedRobot {
  I2CCOM arduino;

  public int m_rainbowFirstPixelHue = 0;

  double motorSpeed = 0.6; //0.55 lowest speed 1 full speed
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

  private boolean switched = false;
  private boolean rainBowLights = false;

  AddressableLED m_led = new AddressableLED(9);
  AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(56);


  DoubleSolenoid head = new DoubleSolenoid(9, PneumaticsModuleType.CTREPCM, 0, 1);

  
  @Override
  public void robotInit() {
    m_right.setInverted(true);

    m_led.setLength(m_ledBuffer.getLength());
    m_led.start();

    head.set(kReverse);

    Thread cameraThread = new Thread( () -> {
      // Get the Camera from CameraServer and set resolution
      UsbCamera camera = CameraServer.startAutomaticCapture();
      camera.setResolution(640, 480);

      CvSink cvSink = CameraServer.getVideo();
      CvSource outputStream = CameraServer.putVideo("Front Camera", 640, 480); // Name Camera On Dashboard And Set Dashboard Resolutiom

      Mat mat = new Mat();

      while (!Thread.interrupted()) {
        if(cvSink.grabFrame(mat) == 0) {
          outputStream.notifyError(cvSink.getError());
          continue;
        }
        outputStream.putFrame(mat);
      }
    });
    cameraThread.setDaemon(true);
    cameraThread.start();

    
  }

  @Override
  public void robotPeriodic() {
    rainbow();
    m_led.setData(m_ledBuffer);
  }

  @Override
  public void teleopInit() {
  //ran when teleop is enabled
  }

  @Override
  public void teleopPeriodic() {
    //System.out.println("Right Limit Switch held: " + rightArmSwitch.get() + ". Left Switch Held: " + leftArmSwitch.get());
    
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
    
    //double speedPot = m_stick.getThrottle();
    //motorSpeed = map(speedPot, 1, -1, 0.55, 1);

    Thread leftArm = new Thread() {
      public void run() {
        while(leftArmSwitch.get() == false) {
          // Emergency Stop the Arm
          if(m_stick.getRawButtonPressed(4)) {
            m_leftArmMotor.set(0);
            return;
          }
          m_leftArmMotor.set(1);
        }
        m_leftArmMotor.set(0);
        delay(200);
        m_leftArmMotor.set(-1);
        delay(550);
        m_leftArmMotor.set(0);
        System.out.println("ThreadCalled");
      }
    };

    Thread rightArm = new Thread() {
      public void run() {
        while(rightArmSwitch.get() == false) {
          // Emergency Stop the Arm
          if(m_stick.getRawButtonPressed(4)) { 
            m_rightArmMotor.set(0);
            return;
          }          
          m_rightArmMotor.set(1);
        }
        m_rightArmMotor.set(0);
        delay(200);
        m_rightArmMotor.set(-1);
        delay(550);
        m_rightArmMotor.set(0);
      }
    };

    Thread leftHalf = new Thread() {
      public void run() {
        if(leftArmSwitch.get() == false) {
          m_leftArmMotor.set(1);
          delay(350);
          m_leftArmMotor.set(0);
          delay(250);
          m_leftArmMotor.set(-1);
          delay(325);
          m_leftArmMotor.set(0); 
        }
      }
    };

    Thread rightHalf = new Thread() {
      public void run() {
        if(rightArmSwitch.get() == false) {
          m_rightArmMotor.set(1);
          delay(350);
          m_rightArmMotor.set(0);
          delay(250);
          m_rightArmMotor.set(-1);
          delay(325);
          m_rightArmMotor.set(0);
        }
      }
    };

    if(m_stick.getRawButtonPressed(3)) {
      if(!rainBowLights) {
        rainBowLights = true;
      } else {
        rainBowLights = false;
      }
    }

    if(m_stick.getRawButtonPressed(7)) {
      System.out.println("left arm toggled");
      leftArm.start();
    }

    if(m_stick.getRawButtonPressed(8)){
      rightArm.start();
    }

    if(m_stick.getRawButtonPressed(9)) {
      leftHalf.start();
    }

    if(m_stick.getRawButtonPressed(10)) {
      rightHalf.start();
    }

    if(m_stick.getRawButtonPressed(1)) {
      head.toggle();
    }

    if(m_stick.getRawButtonPressed(12)) {
      Thread dance = new Thread() {
        public void run() {
          head.toggle();
          delay(500);
          head.toggle();
          leftArmTest();
          rightArmTest();
          head.toggle();
          delay(500);
          head.toggle();
          delay(1000);
          head.toggle();
          delay(500);
          head.toggle();
          leftArmTest();
          delay(1000);
          head.toggle();
          delay(500);
          head.toggle();
          rightArmTest();
        }
      };
      dance.start();
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

  private void rainbow() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
      m_ledBuffer.setHSV(i, hue, 255, 128);
    }
    m_rainbowFirstPixelHue += 3;
    m_rainbowFirstPixelHue %= 180;
  }

/* --------------------------------------------------

TEST CODE

mostly added this cuz I kept getting lost
-------------------------------------------------- */

  private void leftArmTest() {
    Thread leftArmTest = new Thread() {
      public void run() {
        while(leftArmSwitch.get() == false) {
          m_leftArmMotor.set(1);
        }
        m_leftArmMotor.set(0);
        delay(200);
        m_leftArmMotor.set(-1);
        delay(550);
        m_leftArmMotor.set(0);
      }
    };
    leftArmTest.start();
  }

  private void rightArmTest() {
    Thread rightArmTest = new Thread() {
      public void run() {
        while(rightArmSwitch.get() == false) {
          m_rightArmMotor.set(1);
        }
        m_rightArmMotor.set(0);
        delay(200);
        m_rightArmMotor.set(-1);
        delay(550);
        m_rightArmMotor.set(0);
      }
    };
    rightArmTest.start();
  }
}
