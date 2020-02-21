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
  static Devices devices;
  @Test
  public void test(){
      devices = new Devices();
      Devices.Init();

      // The following 3 lines are for desktop usage, assign the Mat image to the camera image when deploying to a robot
      AutoQueue.clearQueue();
      AutoControlData q=new AutoControlData();      

      q.autoState = AutoStates.Drive;
      q.driveState = DriveStates.DriveStart;
      q.gearState = GearStates.HighGearPressed;
      q.LeftDrivePos=5;
      q.LeftDriveSpeed=1.0;
      q.RightDrivePos=1;
      q.RightDriveSpeed=AutoQueue.CalcSpeed;

      Assert.assertEquals(1,AutoQueue.addQueue(q));
      Assert.assertEquals(1,AutoQueue.currentQueue().LeftDriveSpeed,.01);
      Assert.assertEquals(.2,AutoQueue.currentQueue().RightDriveSpeed,.01);
      AutoDrive.Drive();
      AutoDrive.Drive();
      AutoDrive.Drive();
      AutoDrive.Drive();
      Assert.assertEquals(5,Devices.frontLeft.getPosition(),.1);
      AutoDrive.Drive();
      Assert.assertEquals(1,AutoQueue.getSize());
      AutoDrive.Drive();
      Assert.assertEquals(1,AutoQueue.getSize());
      AutoDrive.Drive();
//      Assert.assertEquals(0,AutoQueue.getSize());
//      Assert.assertEquals(DriveStates.Stop,AutoQueue.currentQueue().driveState);
      //TODO fix Stop on autodrive
      //DaBearsCloseDevices.printPWD(devices);
      DaBearsCloseDevices.close(devices);
      }
   }