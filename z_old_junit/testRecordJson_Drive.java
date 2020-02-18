package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testRecordJson_Drive {  
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

      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      drive.drive();
      UserInput.leftStick.setButtonResp(true,true,true);
      UserInput.rightStick.setButtonResp(true,true,true);
      drive.drive();
      Assert.assertEquals(false,AutoRecordJson.WriteLog);

      UserInput.leftStick.setButtonResp(true,true,true);
      UserInput.rightStick.setButtonResp(true,true,true);
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);

      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      drive.drive();
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);

      drive.drive();
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);

      UserInput.leftStick.setButtonResp(true,true,true);
      UserInput.rightStick.setButtonResp(true,true,true);
      drive.drive();
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);

      UserInput.leftStick.setButtonResp(true,true,true);
      UserInput.rightStick.setButtonResp(true,true,true);
      drive.drive();
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);

      UserInput.leftStick.setButtonResp(true,true,true);
      UserInput.rightStick.setButtonResp(true,true,true);
      intake.operate();
      UserInput.leftStick.setButtonResp(true,true,true);
      UserInput.rightStick.setButtonResp(true,true,true);
      lifter.operate();   
      UserInput.leftStick.setButtonResp(true,true,true);
      UserInput.rightStick.setButtonResp(true,true,true);
      drive.drive();
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      drive.drive();
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      drive.drive();
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      AutoRecordJson.AutoRecorder();
      Assert.assertEquals(true,AutoRecordJson.WriteLog);
      drive.drive();
      UserInput.leftStick.setButtonResp(false,false,false);
      UserInput.rightStick.setButtonResp(false,false,false);
      AutoRecordJson.AutoRecorder();

      UserInput.leftStick.setButtonResp(true,true,true);
      UserInput.rightStick.setButtonResp(true,true,true);
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