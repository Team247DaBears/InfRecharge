package frc.robot;

import com.kylecorry.frc.vision.targeting.Target;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

//

public class Main {  
  static Devices devices;
  static AutoShooter autoshooter;
  static CameraStream camerastream;
  private Main() {
}

/**
 * Main initialization function. Do not perform any initialization here.
 *
 * <p>If you change your main robot class, change the parameter type.
 */


public static void main(String... args) {
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
      
      }

}