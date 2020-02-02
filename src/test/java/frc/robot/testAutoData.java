package frc.robot;

import frc.robot.AutoControlData;
import frc.robot.AutoStates;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testAutoData {  
  @Test
  public void test(){
      // The following 3 lines are for desktop usage, assign the Mat image to the camera image when deploying to a robot
      AutoControlData q=new AutoControlData();      

      q.autoState = AutoStates.Drive;
      q.targetState = TargetStates.TargetDrive;
      q.gearState = GearStates.LowGearPressed;
      q.LeftDriveCount=5L;
      q.LeftDriveSpeed=5L;
      q.RightDriveCount=3L;
      q.RightDriveSpeed=-99999999L;

      Assert.assertEquals(1,AutoQueue.addQueue(q));
      q.autoState = AutoStates.Lifter;
      Assert.assertEquals(AutoStates.Drive,AutoQueue.currentQueue().autoState);

      q.LeftDriveCount=0L;
      q.LeftDriveSpeed=0L;
      q.RightDriveCount=7L;
      q.RightDriveSpeed=5L;
      Assert.assertEquals(2,AutoQueue.addQueue(q));

      q.LeftDriveCount=0L;
      q.LeftDriveSpeed=0L;
      q.RightDriveCount=0L;
      q.RightDriveSpeed=0L;
      Assert.assertEquals(3,AutoQueue.addQueue(q));

      q.autoState = AutoStates.Target;
      q.targetState = TargetStates.TargetCollect;
      q.gearState = GearStates.LowGearPressed;
      q.LeftDriveCount=0L;
      q.LeftDriveSpeed=0L;
      q.RightDriveCount=0L;
      q.RightDriveSpeed=0L;
      Assert.assertEquals(4,AutoQueue.addQueue(q));

      System.out.println("WaitingQueue : " + AutoQueue.waitingQueue()); 

      Assert.assertEquals(4,AutoQueue.getSize());
      q = AutoQueue.getQueue();
      Assert.assertEquals(3,AutoQueue.getSize());
      Assert.assertEquals(q.autoState,AutoStates.Drive);

      q = AutoQueue.getQueue();
      Assert.assertEquals(true,(q.LeftDriveSpeed==0L));

      q = AutoQueue.getQueue();
      Assert.assertEquals(true,(q.RightDriveSpeed==0L));

      q=AutoQueue.currentQueue();
      q.autoState=AutoStates.TeleOpt;
      Assert.assertEquals(AutoStates.TeleOpt,AutoQueue.currentQueue().autoState);
      q.autoState=AutoStates.Target;
    
      q = AutoQueue.getQueue();
      Assert.assertEquals(true,(q!=null));
      Assert.assertEquals(true,(q.autoState==AutoStates.Target));
      
      Assert.assertEquals(0,AutoQueue.getSize());
      Assert.assertEquals(AutoStates.TeleOpt,AutoQueue.currentQueue().autoState);
      Assert.assertEquals(AutoStates.TeleOpt,AutoQueue.getQueue().autoState);

      boolean error=false;
      q.LeftDriveSpeed =AutoQueue.MaxSpeed+1;
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // Exceeded Left Max Speed
      error = false;

      q.LeftDriveSpeed =0L;
      q.RightDriveSpeed =AutoQueue.MaxSpeed+1;
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // Exceeded Right Max Speed

      q.LeftDriveSpeed =AutoQueue.CalcSpeed;
      q.RightDriveSpeed =AutoQueue.CalcSpeed;
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // AutoCalc Both Speeds

      error = false;
      q.LeftDriveSpeed =0L;
      q.RightDriveSpeed =0L;
      try {
        int i=0;
        while(i<(AutoQueue.MaxSize+1))
        {
          i = AutoQueue.addQueue(q);
        }
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(AutoQueue.MaxSize, AutoQueue.getSize()); // boundary not met
      Assert.assertEquals(true,error); // boundary not met
    }
   }