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
  private static final String kDefault = "Default";
  private static final String kTestMode = "Test Mode";
  private static final String kLeft = "Left";
  private static final String kCenter = "Center";
  private static final String kCenterDelay = "Center Delay";
  private static final String kRight = "Right";
  private static final String kBackRight = "Backup Right";
  private static final String kBackLeft = "Backup Left";
  private String m_autoSelected;
  private SendableChooser<String> m_chooser = new SendableChooser<>();

  static AutoQueue autoQueue;
  static Drive drive;
  static Devices devices;
  static Intake intake;  
  static AutoIntake autoIntake;       
  static Lifter lifter;
  static UserInput userinput;

  static Shooter shooter;
  
  static CameraStream cameraStream;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption(kDefault, kDefault);
    m_chooser.addOption(kTestMode, kTestMode);
    m_chooser.addOption(kLeft, kLeft);
    m_chooser.addOption(kCenter, kCenter);
    m_chooser.addOption(kCenterDelay, kCenterDelay);
    m_chooser.addOption(kRight, kRight);
    m_chooser.addOption(kBackRight, kBackRight);
    m_chooser.addOption(kBackLeft, kBackLeft);



    autoQueue = new AutoQueue();
    lifter=new Lifter();
    intake=new Intake();
    shooter=new Shooter();
    userinput=new UserInput();
    devices=new Devices();
    autoIntake=new AutoIntake();
    Devices.Init();
    UserInput.Init();
    lifter.Init();
    intake.Init();
    shooter.Init();
    cameraStream = new CameraStream();
    cameraStream.initCamera(); // comment out until camera installed. 
    autoIntake.Init(cameraStream);
    drive=new Drive();    
  }

  @Override
  public void autonomousInit() {
    try {
      m_autoSelected = m_chooser.getSelected();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("Auto selected: " + m_autoSelected);
    switch(m_autoSelected) {
      case kLeft:
        AutoDrive.autonomousInitLeft();
      break;
      case kCenter:
        AutoDrive.autonomousInitCenter();
      break;
      case kCenterDelay:
        AutoDrive.autonomousInitCenterDelay();
      break;
      case kRight:
        AutoDrive.autonomousInitRight();
      break;
      case kBackLeft:
        AutoDrive.autonomousInitBackupLeft();
      break;
      case kBackRight:
        AutoDrive.autonomousInitBackupRight();
      break;
      case kTestMode:
      AutoDrive.autonomousInitDefault();
      break;
      case kDefault:
      AutoDrive.autonomousInitCenterDelay();
      break;
    }
    //AutoDrive.autonomousInitLeft();
    //  AutoDrive.autonomousInitCenter();
    //  AutoDrive.autonomousInitCenterDelay();
    //  AutoDrive.autonomousInitRight();
    //  AutoDrive.autonomousInitBackupLeft();
    //  AutoDrive.autonomousInitBackupRight();
    //AutoDrive.autonomousInitDefault();
    //AutoDrive.autonomousInitCenterDelay();
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
        case AutoShooter: {
          AutoShooter.AutoShoot();
        }
        break;
        case Shooter: {
          shooter.AutoShoot();
        }
        break;
        case AutoIntake: {
          autoIntake.autoIntake();
        }
        break;
        case AutoLifter: {
          // not needed for autonomous
        }
        break;
        case AutoDrive: {
          AutoDrive.Drive();
        }
        break;
      }
}

  @Override
  public void teleopInit() {
    AutoQueue.clearQueue();
  }
  
  int counter=0;
  @Override
  public void teleopPeriodic() {
    manualTeleopPeriodic(); // uncomment for manual only teleop (comment SemiAuto)
    //SemiAutoTeleopPeriodic(); // uncomment for Semi-Autonomous  (comment manual)
  }  

  public void manualTeleopPeriodic() {
    drive.drive(); 
    intake.operate(); 
    lifter.operate();
    shooter.operate();
  }

  public void SemiAutoTeleopPeriodic() {
    AutoStates autoState = AutoStates.TeleOpt;
    autoState = SemiAuto.getAutoState(); // comment this line to disable all semiAuto buttons

    switch (autoState) {
        case TeleOpt: {
          manualTeleopPeriodic(); 
        }
        break;
        case AutoShooter: {
          AutoShooter.AutoShoot();
        }
        break;
        case Shooter: {
          shooter.AutoShoot();
        }
        break;
        case AutoIntake: {
          autoIntake.autoIntake();
        }
        break;
        case AutoLifter: {
          // not needed for autonomous
        }
        break;
        case AutoDrive: {
          AutoDrive.Drive();
        }
        break;
      }
    }

  @Override
  public void testInit() {

  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void disabledPeriodic()
  {
    SmartDashboard.putData("Auto choices", m_chooser);
  }

}
