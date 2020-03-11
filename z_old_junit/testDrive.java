package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;
//
@RunWith(JUnit4.class)
public class testDrive {  
  static Devices devices;
  static UserInput userinput;
  static Drive drive;

  @Test
  public void test(){
      devices=new Devices();
      Devices.Init();
      userinput=new UserInput();
      UserInput.Init();
      drive=new Drive();
      UserInput.leftStick.setOutputs(0); // reset indexes
      double resp[] = {0.0,0.1,0.2,0.3};
      //Arrays.fill(resp, 12.2);
      //UserInput.leftStick.setJoystickResp(resp);
      
      //UserInput.leftStick.setButtonResp(false,false,false);
      //UserInput.rightStick.setButtonResp(false,false,false);
      Assert.assertEquals(0.0,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.LowGearOff,drive.currentGearState);
      Assert.assertEquals(false,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(0.0,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.LowGearPressed,drive.currentGearState);
      Assert.assertEquals(false,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(1,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.HighGearOff,drive.currentGearState);
      Assert.assertEquals(true,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(-0.14,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.LowGearPressed,drive.currentGearState);
      Assert.assertEquals(false,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(0.0,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.LowGearPressed,drive.currentGearState);
      Assert.assertEquals(false,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(0.015,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.HighGearOff,drive.currentGearState);
      Assert.assertEquals(true,Devices.gearShift.get());
      DaBearsCloseDevices.close(devices);
      DaBearsCloseDevices.close(userinput);
      DaBearsCloseDevices.close(drive);
     }

}