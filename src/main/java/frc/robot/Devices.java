/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;


/**
 * This class will contain all code for various devices
 * (Replaces IO)
 */
public class Devices {
    static final boolean UseSparkMax = true;
    static final boolean UseEncoder = true;
      
    private static final int CANFRONTLEFTPWM = 12;
    private static final int CANFRONTRIGHTPWM=5;
    private static final int CANBACKLEFTPWM=3;
    private static final int CANBACKRIGHTPWM=4;    
    private static final int CANSHOOTER=13;

    private static final int FRONTLEFTPWM = 12; // victor
    private static final int FRONTRIGHTPWM=13; // victor
    private static final int BACKLEFTPWM=14;
    private static final int BACKRIGHTPWM=15;    

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

    public static DaBearsSpeedController frontLeft = null;
    public static DaBearsSpeedController frontRight = null;
    public static DaBearsSpeedController backLeft = null;
    public static DaBearsSpeedController backRight = null;

    public static Solenoid gearShift;

    //So, I'll add another set of controls, and you can comment out whichever is unused.    
      public static DoubleSolenoid lifter_solenoid;
      public static DaBearsSpeedController lifter_left_motor;
      public static DaBearsSpeedController lifter_right_motor;

      public static DoubleSolenoid intake_solenoid;
 // public static DaBearsSpeedController intake_motor;
      public static DaBearsSpeedController intake_motor;

      public static DaBearsSpeedController feeder;
      public static DaBearsSpeedController conveyor;
      public static CANSparkMax shooter;
      public static Solenoid shooterAngleControl;


    // Insert member functions

    // Create all objects in the Init function, unless there is some reason it has
    // to wait until the beginning of autonomous.
    // This will be called from robot.init, which executes as soon as the power is
    // applied and the roborio boots up.
    public static void Init() {


       // intake_motor=new DaBearsSpeedController(PWM_INTAKE_MOTOR);
        intake_motor=new DaBearsSpeedController(PWM_INTAKE_MOTOR);
        System.out.println("Inited.");
        intake_solenoid=new DoubleSolenoid(PCM_INTAKE_FORWARD, PCM_INTAKE_BACK);
        
        lifter_solenoid=new DoubleSolenoid(PCM_LIFTER_FORWARD, PCM_LIFTER_BACK);
        lifter_solenoid.set(DoubleSolenoid.Value.kReverse);  //Set default as down
        

        gearShift = new Solenoid(PCM_GEARFORWARD);
        Devices.gearShift.set(false); // set default as low

        if (UseSparkMax) {
          frontLeft=new DaBearsSpeedController(CANFRONTLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,20,21);
          frontRight=new DaBearsSpeedController(CANBACKLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,22,23);

          backLeft=new DaBearsSpeedController(CANFRONTRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,16,17);
          backRight=new DaBearsSpeedController(CANBACKRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,18,19);
        }
        else {
          frontLeft=new DaBearsSpeedController(FRONTLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax);
          frontRight=new DaBearsSpeedController(BACKLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax);

          backLeft=new DaBearsSpeedController(FRONTRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax);
          backRight=new DaBearsSpeedController(BACKRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax);
        }
        frontLeft.setInverted(true);
        backLeft.setInverted(true);

        lifter_left_motor=new DaBearsSpeedController(PWM_LIFTER_LEFT);
        lifter_right_motor=new DaBearsSpeedController(PWM_LIFTER_RIGHT);
        lifter_left_motor.setInverted(true);


        feeder =new DaBearsSpeedController(PWM_SHOOTER_FEEDER);
        conveyor=new DaBearsSpeedController(PWM_SHOOTER_CONVEYOR);

        shooter=new CANSparkMax(CANSHOOTER,MotorType.kBrushless);

        shooterAngleControl=new Solenoid(PCM_SHOOTER_ANGLE);
        
        

      

    }
  }

