package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
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
      UserInput.leftStick.resetIndexes();
      
      Assert.assertEquals(0.0,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.LowGearOff,drive.currentGearState);
      Assert.assertEquals(DoubleSolenoid.Value.kReverse,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(0.0,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.HighGearPressed,drive.currentGearState);
      Assert.assertEquals(DoubleSolenoid.Value.kForward,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(-1.0,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.HighGearOff,drive.currentGearState);
      Assert.assertEquals(DoubleSolenoid.Value.kForward,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(0.37,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.LowGearPressed,drive.currentGearState);
      Assert.assertEquals(DoubleSolenoid.Value.kReverse,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(0.0,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.LowGearPressed,drive.currentGearState);
      Assert.assertEquals(DoubleSolenoid.Value.kReverse,Devices.gearShift.get());
      drive.drive();
      Assert.assertEquals(-0.124,Devices.frontLeft.get(),0.1);
      Assert.assertEquals(GearStates.HighGearOff,drive.currentGearState);
      Assert.assertEquals(DoubleSolenoid.Value.kForward,Devices.gearShift.get());
      
     }

}