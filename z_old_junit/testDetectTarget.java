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
public class testDetectTarget {  
  Devices devices;
  UserInput userinput;

  @Test
  public void test(){
      // The following 3 lines are for desktop usage, assign the Mat image to the camera image when deploying to a robot
      DetectTarget detectTarget = new DetectTarget();
      devices = new Devices();
      Devices.Init();
      userinput = new UserInput();
      UserInput.Init();

      OpenCVManager.getInstance().load(new SystemProperties());
      System.out.println(getClass().getResource("").getFile());
      //System.out.println(Imgcodecs.haveImageReader​(getClass().getResource("frontview2019.png").getFile()));
      
      //Mat image = Imgcodecs.imread(getClass().getResource("frontview2019.png").getFile());
      Mat image = Imgcodecs.imread("2020target.jpg");
        System.out.println("image width:"+image.width());
        System.out.println("image height:"+image.height());

      System.out.println("image width:"+image.toString());
      Target target = detectTarget.detectTopTarget(image);
      System.out.println("target:"+target.toString());

        Assert.assertEquals(true,(target!=null));

        detectTarget.SaveTargetImage("findTopTarget", target, image);

        Assert.assertEquals(6,(int)target.getHorizontalAngle());
        Assert.assertEquals(11,(int)target.getVerticalAngle());
        Assert.assertEquals(6,(int)target.getPercentArea());

        DaBearsCloseDevices.close(devices);
        DaBearsCloseDevices.close(userinput);
  
      }

}