package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testRecordJson {  
  static Drive drive;
  static Devices devices;
  static UserInput userinput;
  static Lifter lifter;
  static Intake intake;
  static AutoRecordJson recordJson;

  @Test
  public void test(){
      intake=new Intake();
      devices=new Devices();
      lifter=new Lifter();
      drive=new Drive();
      userinput=new UserInput();
      recordJson = new AutoRecordJson(devices,intake,lifter,drive,userinput);

      Devices.Init();
      intake.Init();
      lifter.Init();
      UserInput.Init();

      UserInput.leftStick.resetIndexes();

      drive.drive();
      drive.drive();
      Assert.assertEquals(false,AutoRecordJson.WriteLog);

      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(false,AutoRecordJson.WriteLog);
      drive.drive();
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      drive.drive();
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      drive.drive();
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      drive.drive();
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      intake.operate();
      intake.operate();
      lifter.operate();   
      drive.drive();
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      drive.drive();
      drive.drive();
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      drive.drive();
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      drive.drive();
      AutoRecordJson.AutoRecorder();
      //      Assert.assertEquals(3, AutoRecordJson.autodata.size());
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(false,AutoRecordJson.WriteLog);

      DaBearsCloseDevices.close(intake);
      DaBearsCloseDevices.close(devices);
      DaBearsCloseDevices.close(userinput);
      DaBearsCloseDevices.close(lifter);
      DaBearsCloseDevices.close(drive);
      DaBearsCloseDevices.close(recordJson);
     }

}