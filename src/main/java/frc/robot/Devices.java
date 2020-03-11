/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;

import com.revrobotics.CANError;
import com.revrobotics.CANPIDController.ArbFFUnits;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

//import edu.wpi.first.wpilibj.Victor;
//import com.revrobotics.CANSparkMax;
//import com.revrobotics.CANEncoder;
//import com.revrobotics.CANPIDController;


/**
 * This class will contain all code for various devices
 * (Replaces IO)
 */
public class Devices {
    static Boolean isRunningTest = null;
    static  boolean UseSparkMax = true;
    static  boolean UseEncoder = true;

    private static final int CANFRONTLEFTPWM = 12;
    private static final int CANFRONTRIGHTPWM=5;
    private static final int CANBACKLEFTPWM=3;
    private static final int CANBACKRIGHTPWM=4;    
    private static final int CANSHOOTER=13;

    private static final int FRONTLEFTPWM = 12; // victor
    private static final int FRONTRIGHTPWM=13; // victor
    private static final int BACKLEFTPWM=14;
    private static final int BACKRIGHTPWM=15;    
    private static final int SHOOTERPWM=16;

    private static final int PWM_INTAKE_MOTOR=0;
    private static final int PWM_SHOOTER_CONVEYOR = 1;
    private static final int PWM_SHOOTER_FEEDER = 2;
    private static final int PWM_LIFTER_LEFT = 3;
    private static final int PWM_LIFTER_RIGHT= 4;
    private static final int PWM_COLOR_WHEEL = 5;


    public static final int  PCM_INTAKE_FORWARD=0;
    public static final int  PCM_INTAKE_BACK=1;
    private static final int PCM_GEARFORWARD = 2; // solonoid3 pin0
    //private static final int PCM_COLOR_WHEEL = 3; 
    private static final int PCM_SHOOTER_ANGLE = 4; 
    
    public static final int  PCM_LIFTER_FORWARD=5;
    public static final int  PCM_LIFTER_BACK=6;

    public static final double GearSpeedHigh = 1; //0.0841750841750842; //11.88 inches
    public static final double GearSpeedLow = 1; //0.0210368650022299; //1/47.5356;
    
    public static CANSparkMax frontLeftSpark = null;
    public static CANSparkMax frontRightSpark = null;
    public static CANSparkMax backLeftSpark = null;
    public static CANSparkMax backRightSpark = null;

    public static SpeedController frontLeft = null;
    public static SpeedController frontRight = null;
    public static SpeedController backLeft = null;
    public static SpeedController backRight = null;

    public static CANPIDController frontLeftPID = null;
    public static CANPIDController frontRightPID = null;
    public static CANPIDController backLeftPID = null;
    public static CANPIDController backRightPID = null;

    public static CANEncoder frontLeftEncoder = null;
    public static CANEncoder frontRightEncoder = null;
    public static CANEncoder backLeftEncoder = null;
    public static CANEncoder backRightEncoder = null;

    public static Solenoid gearShift;

    //So, I'll add another set of controls, and you can comment out whichever is unused.    
      public static DoubleSolenoid lifter_solenoid;
      public static SpeedController lifter_left_motor;
      public static SpeedController lifter_right_motor;

      public static DoubleSolenoid intake_solenoid;
 // public static DaBearsSpeedController intake_motor;
      public static SpeedController intake_motor;

      public static SpeedController feeder;
      public static SpeedController conveyor;
      //public static CANSparkMax shooter;
      public static SpeedController shooter;
      public static CANSparkMax shooterSpark;
      public static CANEncoder shooterEncoder;
      public static CANPIDController shooterPID;
      public static Solenoid shooterAngleControl;


    // Insert member functions

    // Create all objects in the Init function, unless there is some reason it has
    // to wait until the beginning of autonomous.
    // This will be called from robot.init, which executes as soon as the power is
    // applied and the roborio boots up.
    public static void Init() {
//      if (isRunningTest()) { // if running junit tests... set to test mode
//        UseSparkMax = false;  // use victors if junits
//        UseEncoder = false; // don't use encoder if junits
//      }
        //isRunningTest();
        intake_motor=new Victor(PWM_INTAKE_MOTOR);
        System.out.println("Inited.");
        intake_solenoid=new DoubleSolenoid(PCM_INTAKE_FORWARD, PCM_INTAKE_BACK);
        
        lifter_solenoid=new DoubleSolenoid(PCM_LIFTER_FORWARD, PCM_LIFTER_BACK);
        lifter_solenoid.set(DoubleSolenoid.Value.kReverse);  //Set default as down        

        gearShift = new Solenoid(PCM_GEARFORWARD);
        Devices.gearShift.set(false); // set default as low

        if (UseSparkMax ) {
          if (frontLeftSpark == null) {
            frontLeftSpark = new CANSparkMax(CANFRONTLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
            frontRightSpark = new CANSparkMax(CANFRONTRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless); 
            backLeftSpark = new CANSparkMax(CANBACKLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
            backRightSpark = new CANSparkMax(CANBACKRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
          }
          frontLeft=frontLeftSpark;
          frontLeftEncoder = frontLeftSpark.getEncoder();
          frontLeftPID = frontLeftSpark.getPIDController();

          frontRight=frontRightSpark;
          frontRightEncoder = frontRightSpark.getEncoder();
          frontRightPID = frontRightSpark.getPIDController();

          backLeft=backLeftSpark;
          backLeftEncoder = backLeftSpark.getEncoder();
          backLeftPID = backLeftSpark.getPIDController();

          backRight=backRightSpark;
          backRightEncoder = backRightSpark.getEncoder();
          backRightPID = backRightSpark.getPIDController();
        }
        else {
          frontLeft=new Victor(FRONTLEFTPWM);
          frontRight=new Victor(BACKLEFTPWM);

          backLeft=new Victor(FRONTRIGHTPWM);
          backRight=new Victor(BACKRIGHTPWM);
        }


        frontLeftSpark.restoreFactoryDefaults();
        frontRightSpark.restoreFactoryDefaults();
        backLeftSpark.restoreFactoryDefaults();
        backRightSpark.restoreFactoryDefaults();
        
        frontRight.setInverted(true);
        backRight.setInverted(true);
        frontLeft.setInverted(false);
        backLeft.setInverted(false);

        frontRightSpark.setIdleMode(IdleMode.kCoast);//Temporary, for testing.  For permanent value they should be brake
        frontLeftSpark.setIdleMode(IdleMode.kCoast);
        backRightSpark.setIdleMode(IdleMode.kCoast);
        backLeftSpark.setIdleMode(IdleMode.kCoast);

        lifter_left_motor=new Victor(PWM_LIFTER_LEFT);
        lifter_right_motor=new Victor(PWM_LIFTER_RIGHT);
        lifter_left_motor.setInverted(true);

        feeder =new Victor(PWM_SHOOTER_FEEDER);
        conveyor=new Victor(PWM_SHOOTER_CONVEYOR);

        if (UseSparkMax) {
          //shooter=new CANSparkMax(CANSHOOTER,MotorType.kBrushless);
        shooterSpark=new CANSparkMax(CANSHOOTER,MotorType.kBrushless);
        shooter = shooterSpark;
        shooterPID = shooterSpark.getPIDController();
        shooterEncoder = shooterSpark.getEncoder();
        }
        else {
          shooter=new Victor(SHOOTERPWM);
        }
        shooterAngleControl=new Solenoid(PCM_SHOOTER_ANGLE);
        
        

      

    }
    public static void setMotorConversionLow() {
      frontLeftEncoder.setPositionConversionFactor(GearSpeedLow);
      frontRightEncoder.setPositionConversionFactor(GearSpeedLow);
      backLeftEncoder.setPositionConversionFactor(GearSpeedLow);
      backRightEncoder.setPositionConversionFactor(GearSpeedLow);
    }
    public static void setMotorConversionHigh() {
      frontLeftEncoder.setPositionConversionFactor(GearSpeedHigh);
      frontRightEncoder.setPositionConversionFactor(GearSpeedHigh);
      backLeftEncoder.setPositionConversionFactor(GearSpeedHigh);
      backRightEncoder.setPositionConversionFactor(GearSpeedHigh);
    }
    static Boolean isRunningTest() {
      if (isRunningTest == null) {
          isRunningTest = true;
          try {
              Class.forName("org.junit.Test");
          } catch (ClassNotFoundException e) {
              isRunningTest = false;
          }
      }
      return isRunningTest;
  }
  
  public static void InitEncoderController(Object Spark,Double KP, Double KD, Double KI, Double MINOUT, Double MAXOUT, Double IZONE, Double FFVALUE, Double TARGETRPM, int i, int j) {
    CANSparkMax motor = (CANSparkMax)Spark;
    // motor.restoreFactoryDefaults();
      motor.set(i);
      motor.getPIDController().setP(KP);
      motor.getPIDController().setD(KD);
      motor.getPIDController().setI(KI);
      motor.getPIDController().setOutputRange(MINOUT, MAXOUT);
      motor.getPIDController().setIZone(IZONE);
      motor.getPIDController().setFF(FFVALUE / TARGETRPM);
      motor.getEncoder().setPosition(j);
  }

  }

