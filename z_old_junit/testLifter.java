package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testLifter {  
  static Devices devices;
  static UserInput userinput;
  static Lifter lifter;

  @Test
  public void test(){
      devices=new Devices();
      Devices.Init();
      userinput=new UserInput();
      UserInput.Init();
      lifter=new Lifter();
      UserInput.leftStick.resetIndexes();

      Assert.assertEquals(LifterStates.LifterDown,lifter.heldPosition);
      Assert.assertEquals(0,Devices.lifterHoist.get(),0.1);
      Assert.assertEquals(DoubleSolenoid.Value.kReverse,Devices.highLifter.get());
      Assert.assertEquals(DoubleSolenoid.Value.kReverse,Devices.lowLifter.get());
      lifter.setPosition();
      Assert.assertEquals(LifterStates.Up,lifter.heldPosition);
      Assert.assertEquals(0,Devices.lifterHoist.get(),0.1);
      Assert.assertEquals(DoubleSolenoid.Value.kForward,Devices.highLifter.get());
      Assert.assertEquals(DoubleSolenoid.Value.kForward,Devices.lowLifter.get());

      lifter.setPosition();
      Assert.assertEquals(LifterStates.Down,lifter.heldPosition);
      Assert.assertEquals(0,Devices.lifterHoist.get(),0.1);
      Assert.assertEquals(DoubleSolenoid.Value.kReverse,Devices.highLifter.get());
      Assert.assertEquals(DoubleSolenoid.Value.kReverse,Devices.lowLifter.get());

      lifter.setPosition();
      Assert.assertEquals(LifterStates.Up,lifter.heldPosition);
      Assert.assertEquals(0,Devices.lifterHoist.get(),0.1);
      Assert.assertEquals(DoubleSolenoid.Value.kForward,Devices.highLifter.get());
      Assert.assertEquals(DoubleSolenoid.Value.kForward,Devices.lowLifter.get());

      lifter.setPosition();
      Assert.assertEquals(LifterStates.Hoist,lifter.heldPosition);
      Assert.assertEquals(1,Devices.lifterHoist.get(),0.1);
      Assert.assertEquals(DoubleSolenoid.Value.kForward,Devices.highLifter.get());
      Assert.assertEquals(DoubleSolenoid.Value.kForward,Devices.lowLifter.get());

      DaBearsCloseDevices.close(devices);
      DaBearsCloseDevices.close(userinput);
      DaBearsCloseDevices.close(lifter);
  }
}