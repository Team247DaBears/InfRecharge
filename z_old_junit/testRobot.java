package frc.robot;

import javax.lang.model.util.ElementScanner6;
import frc.robot.AutoControlData;
import frc.robot.AutoStates;

import java.sql.Time;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
//
@RunWith(JUnit4.class)
public class testRobot {
  Robot robot;
  static UserInput userinput;
  static Devices devices;
  static Devices shooter;
  long autoBeginTime;
  long elapsed;
  static AutoQueue autoQueue;
  @Test
  public void test(){
      robot = new Robot();
      userinput=new UserInput();
      UserInput.Init();
      UserInput.leftStick.resetIndexes();
      robot.robotInit();
      System.out.println("size:"+AutoQueue.getSize());
      robot.autonomousInit();
      autoBeginTime=System.currentTimeMillis();
      elapsed=System.currentTimeMillis()-autoBeginTime;
      while (elapsed<15000) {
//        System.out.println(AutoQueue.currentQueue());

        robot.autonomousPeriodic();
        Double frontLeftPos = devices.frontLeft.getPosition();
        Double frontRightPos = devices.frontRight.getPosition();
        AutoControlData q = AutoQueue.currentQueue();
//       System.out.println("diff:"+(java.lang.Math.abs(q.LeftDrivePos - frontLeftPos)));
//       System.out.println("diff:"+(java.lang.Math.abs(q.RightDrivePos - frontRightPos)));
        elapsed=System.currentTimeMillis()-autoBeginTime;
      }
      System.out.println("feeder"+Devices.feeder.get());
      System.out.println("After Periodic size:"+AutoQueue.getSize());
      robot.teleopInit();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());
      UserInput.leftStick.resetIndexes();

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      UserInput.leftStick.resetIndexes();
      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

      robot.teleopPeriodic();
      System.out.println("TeliopInit size:"+AutoQueue.getSize());
      System.out.println("feeder"+Devices.feeder.get());

    DaBearsCloseDevices.close(Robot.devices);
    //DaBearsCloseDevices.close(Robot.cameraStream);
    DaBearsCloseDevices.close(Robot.lifter);
    DaBearsCloseDevices.close(Robot.shooter);
    DaBearsCloseDevices.close(Robot.drive);
    DaBearsCloseDevices.close(Robot.userinput);
    DaBearsCloseDevices.close(Robot.detecttarget);
      }
   }