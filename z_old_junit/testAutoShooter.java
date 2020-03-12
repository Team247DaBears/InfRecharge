package frc.robot;

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
  static Devices devices;
  static AutoShooter autoshooter;
  static CameraStream camerastream;

  @Test
  public void test(){
      // The following 3 lines are for desktop usage, assign the Mat image to the camera image when deploying to a robot
      devices = new Devices();
      Devices.Init();
      camerastream = new CameraStream();
      camerastream.initCamera(); // do this a second time to get the shooter images
      autoshooter = new AutoShooter();
        AutoShooter.Init(camerastream); // get image list for toptarget
//      autointake.Init(camerastream); // get image list for intake
      AutoQueue.clearQueue();
System.out.println("first shot");
AutoQueue.addAutoShooterQueue();
      
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();

      long autoBeginTime=System.currentTimeMillis();
      long elapsed=System.currentTimeMillis()-autoBeginTime;
      //while (elapsed<56 && (AutoQueue.getSize()>0)) {
      //  AutoShooter.AutoShoot();
      //}

      System.out.println("second shot");
      AutoQueue.addAutoShooterQueue();
            
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();
      AutoShooter.AutoShoot();
      
            autoBeginTime=System.currentTimeMillis();
            elapsed=System.currentTimeMillis()-autoBeginTime;
         //   while (elapsed<56 && (AutoQueue.getSize()>0)) {
         //       AutoShooter.AutoShoot();
         //   }
      
        DaBearsCloseDevices.close(devices);
        DaBearsCloseDevices.close(autoshooter);  
      }

}