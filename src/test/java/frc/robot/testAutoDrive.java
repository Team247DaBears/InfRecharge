package frc.robot;

import frc.robot.AutoControlData;
import frc.robot.AutoStates;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testAutoDrive {  
  @Test
  public void test(){
      Devices devices = new Devices();
      Devices.Init();

      // The following 3 lines are for desktop usage, assign the Mat image to the camera image when deploying to a robot
      AutoQueue.clearQueue();
      AutoControlData q=new AutoControlData();      

      q.autoState = AutoStates.Drive;
      q.driveState = DriveStates.Drive;
      q.gearState = GearStates.HighGearPressed;
      q.LeftDriveCount=5;
      q.LeftDriveSpeed=5.0;
      q.RightDriveCount=5;
      q.RightDriveSpeed=AutoQueue.CalcSpeed;

      Assert.assertEquals(1,AutoQueue.addQueue(q));
      AutoDrive.Drive();
      AutoDrive.Drive();
//      Assert.assertEquals(5,(int)Devices.leftRollerMotor.get());
//      Assert.assertEquals(5,(int)Devices.rightRollerMotor.get());
      Assert.assertEquals(3,AutoQueue.currentQueue().LeftDriveCount);
      Assert.assertEquals(3,AutoQueue.currentQueue().RightDriveCount);
      AutoDrive.Drive();
      AutoDrive.Drive();
      Assert.assertEquals(1,AutoQueue.currentQueue().LeftDriveCount);
      AutoDrive.Drive();
      Assert.assertEquals(0,AutoQueue.getSize());
      Assert.assertEquals(DriveStates.Stop,AutoQueue.currentQueue().driveState);
    }
   }