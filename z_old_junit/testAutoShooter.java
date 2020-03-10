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
public class testAutoShooter {  
  Devices devices;
  AutoShooter autoshooter;
  CameraStream camerastream;

  @Test
  public void test(){
      // The following 3 lines are for desktop usage, assign the Mat image to the camera image when deploying to a robot
      DetectTarget detectTarget = new DetectTarget();
      devices = new Devices();
      Devices.Init();
      camerastream = new CameraStream();
      camerastream.initCamera();
      camerastream.initCamera(); // do this a second time to get the shooter images
      detectTarget.Init(camerastream);
      autoshooter = new AutoShooter();
      autoshooter.Init(camerastream); // get image list for toptarget
//      autointake.Init(camerastream); // get image list for intake

System.out.println("first shot");
AutoQueue.addAutoShooterQueue(AutoStates.Shooter, AutoShooterStates.shooterTarget);
      
      autoshooter.AutoShoot();
      autoshooter.AutoShoot();
      autoshooter.AutoShoot();
      autoshooter.AutoShoot();
      autoshooter.AutoShoot();
      autoshooter.AutoShoot();

      long  autoBeginTime=System.currentTimeMillis();
      long elapsed=System.currentTimeMillis()-autoBeginTime;
      while (elapsed<5600 && (AutoQueue.getSize()>0)) {
          autoshooter.AutoShoot();
      }

      System.out.println("second shot");
      AutoQueue.addAutoShooterQueue(AutoStates.Shooter, AutoShooterStates.shooterTarget);
            
            autoshooter.AutoShoot();
            autoshooter.AutoShoot();
            autoshooter.AutoShoot();
            autoshooter.AutoShoot();
            autoshooter.AutoShoot();
            autoshooter.AutoShoot();
      
            autoBeginTime=System.currentTimeMillis();
            elapsed=System.currentTimeMillis()-autoBeginTime;
            while (elapsed<5600 && (AutoQueue.getSize()>0)) {
                autoshooter.AutoShoot();
            }
      
        DaBearsCloseDevices.close(devices);
        DaBearsCloseDevices.close(autoshooter);  
      }

}