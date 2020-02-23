package frc.robot;

import frc.robot.AutoControlData;
import frc.robot.AutoStates;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testRobot {
  Robot robot;
  @Test
  public void test(){
      robot = new Robot();
      robot.robotInit();
      System.out.println("size:"+AutoQueue.getSize());
      robot.autonomousInit();
      System.out.println("size:"+AutoQueue.getSize());
      robot.autonomousPeriodic();
      System.out.println("size:"+AutoQueue.getSize());
      robot.autonomousPeriodic();
      System.out.println("size:"+AutoQueue.getSize());
      robot.autonomousPeriodic();
      robot.teleopInit();
      System.out.println("size:"+AutoQueue.getSize());
      robot.teleopPeriodic();
      System.out.println("size:"+AutoQueue.getSize());

    
    DaBearsCloseDevices.close(Robot.devices);
    //DaBearsCloseDevices.close(Robot.cameraStream);
    DaBearsCloseDevices.close(Robot.lifter);
    DaBearsCloseDevices.close(Robot.shooter);
    DaBearsCloseDevices.close(Robot.drive);
    DaBearsCloseDevices.close(Robot.userinput);
    DaBearsCloseDevices.close(Robot.detecttarget);
      }
   }