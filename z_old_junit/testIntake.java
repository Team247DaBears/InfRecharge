package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testIntake {  
  static Devices devices;
  static UserInput userinput;
  static Intake intake;

  @Test
  public void test(){
      devices=new Devices();
      Devices.Init();
      userinput=new UserInput();
      UserInput.Init();
      intake=new Intake();
      intake.Init();
      UserInput.leftStick.resetIndexes();
      Assert.assertEquals(0.0,intake.intakeMotor.get(),.01);
      intake.operate();
      Assert.assertEquals(DoubleSolenoid.Value.kReverse,intake.intakeSolenoid.get());
      Assert.assertEquals(.5,intake.intakeMotor.get(),.01);
      intake.operate();
      Assert.assertEquals(DoubleSolenoid.Value.kForward,intake.intakeSolenoid.get());
      Assert.assertEquals(0.5,intake.intakeMotor.get(),.01);
      intake.operate();
      Assert.assertEquals(DoubleSolenoid.Value.kForward,intake.intakeSolenoid.get());
      Assert.assertEquals(0.0,intake.intakeMotor.get(),.01);
      intake.operate();
      Assert.assertEquals(DoubleSolenoid.Value.kForward,intake.intakeSolenoid.get());
      Assert.assertEquals(0.0,intake.intakeMotor.get(),.01);
      intake.operate();
      Assert.assertEquals(DoubleSolenoid.Value.kOff,intake.intakeSolenoid.get());
      Assert.assertEquals(0.0,intake.intakeMotor.get(),.01);
      DaBearsCloseDevices.close(intake);
      DaBearsCloseDevices.close(devices);
      DaBearsCloseDevices.close(userinput);
     }

}