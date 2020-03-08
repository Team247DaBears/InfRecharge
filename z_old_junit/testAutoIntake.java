package frc.robot;

import frc.robot.DetectTarget;
import com.kylecorry.frc.vision.targeting.Target;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

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
      DetectTarget detectTarget = new DetectTarget();
      devices = new Devices();
      Devices.Init();
      camerastream = new CameraStream();
      camerastream.initCamera();
      detectTarget.Init(camerastream);
      autointake = new AutoIntake();
      autointake.Init(camerastream,detectTarget); // get image list for toptarget
//      autointake.Init(camerastream); // get image list for intake

      AutoQueue.addIntakeQueue(AutoStates.Intake, IntakeStates.intakeRun);
      
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