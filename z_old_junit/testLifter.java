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
      //devices=new Devices();
      //Devices.Init();
      //userinput=new UserInput();
      //UserInput.Init();
      //lifter=new Lifter();
      //UserInput.leftStick.resetIndexes();

      DaBearsCloseDevices.close(devices);
      DaBearsCloseDevices.close(userinput);
      DaBearsCloseDevices.close(lifter);
  }
}