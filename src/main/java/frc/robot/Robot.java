/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/*  Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.AutoControlData;

import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  static Drive drive;
  static Devices devices;
  static Intake intake;  
       
  static Lifter lifter;
  static UserInput userinput;

  static Shooter shooter;
  
  static CameraStream cameraStream;
  static DetectTarget detecttarget;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    System.out.println("Command Position:"); 

   // lifter=new Lifter();
    intake=new Intake();
    shooter=new Shooter();
    userinput=new UserInput();
    devices=new Devices();
    Devices.Init();
    UserInput.Init();
    //lifter.Init();
//    intake.Init();
//    shooter.Init();
    cameraStream = new CameraStream();
    cameraStream.initCamera();
    detecttarget = new DetectTarget();
    detecttarget.Init(cameraStream);

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

          drive.drive(); 
  //        intake.operate(); 
   //       lifter.operate();
  //        shooter.operate();
          detecttarget.shootTopTarget(); // when implemented it will switch to autonomous mode

//          AutoRecordJson.AutoRecorder(); // records userinput & writes file Takes two buttons to turn on//

        }
        case Target: {
          detecttarget.AutoTarget();
        }
        case Shooter: {
          shooter.AutoShoot();
        }
        case Lifter: {
          
        }
        case Collecter: {
          
        }
        case Drive: {
          AutoDrive.Drive();
        }
      }
    }  

  @Override
  public void testInit() {
    RobotTestModes.testJoysticks(userinput);
    RobotTestModes.testDevices(devices);
  }

  @Override
  public void testPeriodic() {
  }

}
