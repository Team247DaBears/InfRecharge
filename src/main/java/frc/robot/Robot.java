/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/*  Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.AutoControlData;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "Auto Mode";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  static AutoQueue autoQueue;
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
    m_chooser.setDefaultOption("test mode", kDefaultAuto);
    m_chooser.addOption("test mode", kCustomAuto);
    m_chooser.addOption("left", kCustomAuto);
    m_chooser.addOption("center", kCustomAuto);
    m_chooser.addOption("center delay", kCustomAuto);
    m_chooser.addOption("right", kCustomAuto);
    m_chooser.addOption("backup right", kCustomAuto);
    m_chooser.addOption("backup left", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_autoSelected = m_chooser.getSelected();

    autoQueue = new AutoQueue();
    lifter=new Lifter();
    intake=new Intake();
    shooter=new Shooter();
    userinput=new UserInput();
    devices=new Devices();
    Devices.Init();
    UserInput.Init();
    lifter.Init();
    intake.Init();
    shooter.Init();
    cameraStream = new CameraStream();
    cameraStream.initCamera(); // comment out until camera installed. 
    detecttarget = new DetectTarget();
    detecttarget.Init(cameraStream);
    drive=new Drive();    
  }

  @Override
  public void autonomousInit() {
    //m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
    switch(m_autoSelected) {
      case "left":
        AutoDrive.autonomousInitLeft();
      break;
      case "center":
        AutoDrive.autonomousInitCenter();
      break;
      case "center delay":
        AutoDrive.autonomousInitCenterDelay();
      break;
      case "right":
        AutoDrive.autonomousInitRight();
      break;
      case "backup left":
        AutoDrive.autonomousInitBackupLeft();
      break;
      case "backup right":
        AutoDrive.autonomousInitBackupRight();
      break;
      case "Default":
      AutoDrive.autonomousInitCenterDelay();
      //      AutoDrive.autonomousInitDefault();
      break;
    }
  }

  @Override
  public void autonomousPeriodic() {
    //System.out.println("auto size:"+AutoQueue.getSize());

    AutoControlData autoControlData = AutoQueue.currentQueue();
//    System.out.println(autoControlData.toJson());
    switch (autoControlData.autoState) {
        case TeleOpt: {
          // do nothing in autonomous mode
        }
        break;
        case Target: {
          detecttarget.AutoTarget();
        }
        break;
        case Shooter: {
          shooter.AutoShoot();
        }
        break;
        case Intake: {
          intake.AutoIntake();
        }
        break;
        case Lifter: {
          // not needed for autonomous
        }
        break;
        case Drive: {
          AutoDrive.Drive();
        }
        break;
      }
}

  @Override
  public void teleopInit() {
 //   AutoQueue.clearQueue();
  }
  
  int counter=0;
  @Override
  public void teleopPeriodic() {
    AutoControlData autoControlData = AutoQueue.currentQueue();
    switch (autoControlData.autoState) {
        case TeleOpt: {
          drive.drive(); 
          intake.operate(); 
          lifter.operate();
          shooter.operate();
//          detecttarget.shootTopTarget(); // semi-autonomous shoot target (using camera)

//          AutoRecordJson.AutoRecorder(); // records userinput & writes file Takes two buttons to turn on//

        }
        break;
        case Target: {
          detecttarget.AutoTarget();
        }
        break;
        case Shooter: {
          shooter.AutoShoot();
        }
        break;
        case Lifter: {
          //lifter.AutoLift();          
        }
        break;
        case Intake: {
          intake.AutoIntake();
        }
        break;
        case Drive: {
          AutoDrive.Drive();
        }
        break;
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
