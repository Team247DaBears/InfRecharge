/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/*  Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


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
  private SendableChooser<String> m_chooser = new SendableChooser<>();

  static Drive drive;
  static Devices devices;
  static Intake intake;  
       
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
    SmartDashboard.putData("Auto choices", m_chooser);


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
    drive=new Drive();    
  }

  @Override
  public void autonomousInit()
  {

  }
 

  @Override
  public void autonomousPeriodic() {
 
  }

  @Override
  public void teleopInit() {
  }
  
  @Override
  public void teleopPeriodic() {
          drive.drive(); 
          intake.operate(); 
          lifter.operate();
          shooter.operate();
    }  

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
