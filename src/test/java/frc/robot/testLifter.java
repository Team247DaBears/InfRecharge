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
public class testLifter {  
  static Devices devices;
  static UserInput userinput;
  static Lifter lifter;

  @Test
  public void test(){
      devices=new Devices();
      Devices.Init();
      userinput=new UserInput();
      UserInput.Init();
      lifter=new Lifter();
      UserInput.leftStick.resetIndexes();

      Assert.assertEquals(LifterStates.Down,lifter.heldPosition);

      lifter.setPosition();
      Assert.assertEquals(LifterStates.Up,lifter.heldPosition);
      lifter.setPosition();
      Assert.assertEquals(LifterStates.Down,lifter.heldPosition);
      lifter.setPosition();
      Assert.assertEquals(LifterStates.Up,lifter.heldPosition);
      lifter.setPosition();
      Assert.assertEquals(LifterStates.Hoist,lifter.heldPosition);
     }

}