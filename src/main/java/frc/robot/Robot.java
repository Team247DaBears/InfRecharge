/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/*  Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.AutoControlData;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  Drive drive;
  Devices devices;
  
       
  Lifter lifter;
  static DriverStation driverstation;
  
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    System.out.println("Command Position:"); 
    lifter=new Lifter();
    driverstation=new DriverStation();
    devices=new Devices();
    Devices.Init();
    DriverStation.Init();
    drive=new Drive();
    
   

    
  }

  @Override
  public void autonomousInit() {
  AutoDrive.autonomousModeInit();
  }

  @Override
  public void autonomousPeriodic() {
    AutoDrive.Drive();
}

  @Override
  public void teleopInit() {
  }

  int counter=0;
  @Override
  public void teleopPeriodic() {
    AutoControlData autoControlData = AutoQueue.currentQueue();
      switch (autoControlData.autoState) {
        case TeleOpt: {
          lifter.setPosition();   
          drive.drive();
        }
        case Target: {

        }
        case Lifter: {
          
        }
        case Collecter: {
          
        }
        case Drive: {
          
        }
      }
    }  

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
