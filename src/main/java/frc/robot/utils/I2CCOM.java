package frc.robot.utils;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class I2CCOM {

  public int deviceAddress;

  public I2C wire;

  public I2CCOM(int deviceAddress) {
    // 1 = the arduino
    this.deviceAddress = deviceAddress;
    this.wire = new I2C(Port.kOnboard, this.deviceAddress);
  }

  public int getData() {
    //return this.wire.read;
    return 0;
  }
  public void sendData(int address, int data) {
    this.wire.write(address, data);
  }

}
