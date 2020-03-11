package frc.robot;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testAutoIntake {  
  Devices devices;
  AutoIntake autointake;
  CameraStream camerastream;

  @Test
  public void test(){
      // The following 3 lines are for desktop usage, assign the Mat image to the camera image when deploying to a robot
      devices = new Devices();
      Devices.Init();
      camerastream = new CameraStream();
      camerastream.initCamera();
      autointake = new AutoIntake();
      autointake.Init(camerastream); // get image list for toptarget
//      autointake.Init(camerastream); // get image list for intake
      AutoQueue.clearQueue();
      AutoQueue.addIntakeQueue(AutoStates.Intake, AutoIntakeStates.intakeRun);
      
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();
      autointake.autoIntake();

        DaBearsCloseDevices.close(devices);
        DaBearsCloseDevices.close(autointake);  
      }

}