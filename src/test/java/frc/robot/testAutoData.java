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
      AutoQueue.clearQueue();
      AutoControlData q=new AutoControlData();      

      q.autoState = AutoStates.Drive;
      q.targetState = TargetStates.TargetDrive;
      q.gearState = GearStates.LowGearPressed;
      q.LeftDrivePos=5;
      q.LeftDriveSpeed=1.0;
      q.RightDrivePos=3;
      q.RightDriveSpeed=AutoQueue.CalcSpeed;

      Assert.assertEquals(1,AutoQueue.addQueue(q));
      q.autoState = AutoStates.Lifter;
      Assert.assertEquals(AutoStates.Drive,AutoQueue.currentQueue().autoState);

      q.LeftDrivePos=0;
      q.LeftDriveSpeed=0.0;
      q.RightDrivePos=7;
      q.RightDriveSpeed=1.0;
      Assert.assertEquals(2,AutoQueue.addQueue(q));

      q.LeftDrivePos=0;
      q.LeftDriveSpeed=0.0;
      q.RightDrivePos=0;
      q.RightDriveSpeed=0.0;
      Assert.assertEquals(3,AutoQueue.addQueue(q));

      q.autoState = AutoStates.Target;
      q.targetState = TargetStates.TargetCollect;
      q.gearState = GearStates.LowGearPressed;
      q.LeftDrivePos=0;
      q.LeftDriveSpeed=0.0;
      q.RightDrivePos=0;
      q.RightDriveSpeed=0.0;
      Assert.assertEquals(4,AutoQueue.addQueue(q));

      System.out.println("WaitingQueue : " + AutoQueue.waitingQueue()); 

      Assert.assertEquals(4,AutoQueue.getSize());
      q = AutoQueue.getQueue();
      Assert.assertEquals(3,AutoQueue.getSize());
      Assert.assertEquals(q.autoState,AutoStates.Drive);

      q = AutoQueue.getQueue();
      Assert.assertEquals(0,q.LeftDriveSpeed,0);

      q = AutoQueue.getQueue();
      Assert.assertEquals(0,q.RightDriveSpeed,0);

      q=AutoQueue.currentQueue();
      q.autoState=AutoStates.TeleOpt;
      Assert.assertEquals(AutoStates.TeleOpt,AutoQueue.currentQueue().autoState);
      q.autoState=AutoStates.Target;
    
      q = AutoQueue.getQueue();
      Assert.assertNotNull(q);
      Assert.assertEquals(AutoStates.Target,q.autoState);
      
      Assert.assertEquals(0,AutoQueue.getSize());
      Assert.assertEquals(0,AutoQueue.removeCurrent());
      q=AutoQueue.getQueue();
      Assert.assertEquals(0,AutoQueue.getSize());
      Assert.assertEquals(AutoStates.TeleOpt,AutoQueue.currentQueue().autoState);
      Assert.assertEquals(AutoStates.TeleOpt,AutoQueue.getQueue().autoState);

      boolean error=false;
      q.LeftDriveSpeed =AutoQueue.MaxSpeed+.1;
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // Exceeded Left Max Speed
      error = false;

      q.LeftDriveSpeed =-1*(AutoQueue.MaxSpeed+.1);
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // Below Left Min Speed
      error = false;
      
      q.LeftDriveSpeed =0.0;
      q.RightDriveSpeed =AutoQueue.MaxSpeed+.1;
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // Exceeded Right Max Speed

      q.LeftDriveSpeed =0.0;
      q.RightDriveSpeed =-1*(AutoQueue.MaxSpeed+.1);
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // Below Right Min Speed

      error = false;
      q.LeftDrivePos =AutoQueue.MinDistance-1;
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // below Left Min Distance

      error = false;
      q.LeftDrivePos =AutoQueue.MaxDistance+1;
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // above Right Max Distance

      error = false;
      q.LeftDrivePos =0;
      q.RightDrivePos =AutoQueue.MinDistance-1;
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // below Right Min Distance

      error = false;
      q.RightDrivePos =AutoQueue.MinDistance+1;
      try {
        AutoQueue.addQueue(q);
      }
      catch (Exception e) {
        System.out.println("error encountered:" + e.getMessage());          
        error = true;
      }
      Assert.assertEquals(true,error); // above Right Max Distance

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
      q.LeftDriveSpeed =0.0;
      q.RightDriveSpeed =0.0;
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