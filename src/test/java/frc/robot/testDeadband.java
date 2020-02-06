package frc.robot;
import frc.robot.DriverStation;
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
public class testDeadband{
  @Test
  public void test(){
     

    Assert.assertEquals(.1,DriverStation.getDeadband(.3),.025);
    Assert.assertEquals(.25,DriverStation.getDeadband(.4),.026);
    Assert.assertEquals(.37,DriverStation.getDeadband(.5),.025);
    Assert.assertEquals(-.1,DriverStation.getDeadband(-.3),.025);
    Assert.assertEquals(-.25,DriverStation.getDeadband(-.4),.026);
    Assert.assertEquals(-.37,DriverStation.getDeadband(-.5),.025);
        Assert.assertEquals(0,DriverStation.getDeadband(-.16),.0);
        Assert.assertEquals(-.01,DriverStation.getDeadband(-.21),.04);
        Assert.assertEquals(-.87,DriverStation.getDeadband(-.9),.025);
        Assert.assertEquals(.93,DriverStation.getDeadband(.95),.0225);
       
      }
      
      

}