/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;

/**
 * This class will contain all code for various devices
 * (Replaces IO)
 */
public class Devices {


    private static final int CANFRONTLEFT = 12;
    private static final int CANFRONTRIGHT=5;
    private static final int CANBACKLEFT=3;
    private static final int CANBACKRIGHT=4;    
    private static final int CANSHOOTER=13;

    private static final int PWM_INTAKE_MOTOR=0;
    private static final int PWM_SHOOTER_CONVEYOR = 1;
    private static final int PWM_SHOOTER_FEEDER = 2;
    private static final int PWM_LIFTER_LEFT = 3;
    private static final int PWM_LIFTER_RIGHT= 4;
   


    public static final int  PCM_INTAKE_FORWARD=0;
    public static final int  PCM_INTAKE_BACK=1;
    private static final int PCM_GEARFORWARD = 2; // solonoid3 pin0
 
    private static final int PCM_SHOOTER_ANGLE = 4; 
    
    public static final int  PCM_LIFTER_FORWARD=5;
    public static final int  PCM_LIFTER_BACK=6;

    public static final double GEARSPEEDHIGH = 1; //0.0841750841750842; //11.88 inches
    public static final double GEARSPEEDLOW = 1; //0.0210368650022299; //1/47.5356;
    
    public static CANSparkMax frontLeft = null;
    public static CANSparkMax frontRight = null;
    public static CANSparkMax backLeft = null;
    public static CANSparkMax backRight = null;

    public static Solenoid gearShift;

   
      public static DoubleSolenoid lifter_solenoid;
      public static Spark lifter_left_motor;
      public static Spark lifter_right_motor;

      public static DoubleSolenoid intake_solenoid;

      public static Spark intake_motor;

      public static Spark feeder;
      public static Spark conveyor;
      public static CANSparkMax shooter;
      public static Solenoid shooterAngleControl;


    // Insert member functions

    // Create all objects in the Init function, unless there is some reason it has
    // to wait until the beginning of autonomous.
    // This will be called from robot.init, which executes as soon as the power is
    // applied and the roborio boots up.
    public static void Init() {

        intake_motor=new Spark(PWM_INTAKE_MOTOR);
        intake_solenoid=new DoubleSolenoid(PCM_INTAKE_FORWARD, PCM_INTAKE_BACK);
        
        lifter_solenoid=new DoubleSolenoid(PCM_LIFTER_FORWARD, PCM_LIFTER_BACK);
        lifter_solenoid.set(DoubleSolenoid.Value.kReverse);  //Set default as down        

        gearShift = new Solenoid(PCM_GEARFORWARD);
        Devices.gearShift.set(false); // set default as low

        frontLeft=new CANSparkMax(CANFRONTLEFT,MotorType.kBrushless);
        frontRight=new CANSparkMax(CANFRONTRIGHT,MotorType.kBrushless);
        backLeft=new CANSparkMax(CANBACKLEFT,MotorType.kBrushless);
        backRight=new CANSparkMax(CANBACKRIGHT,MotorType.kBrushless);
        
        frontLeft.restoreFactoryDefaults();
        frontRight.restoreFactoryDefaults();
        backLeft.restoreFactoryDefaults();
        backRight.restoreFactoryDefaults();
        
        frontRight.setInverted(true);
        backRight.setInverted(true);
        frontLeft.setInverted(false);
        backLeft.setInverted(false);

        frontRight.setIdleMode(IdleMode.kBrake);//Temporary, for testing.  For permanent value they should be brake
        frontLeft.setIdleMode(IdleMode.kBrake);
        backRight.setIdleMode(IdleMode.kBrake);
        backLeft.setIdleMode(IdleMode.kBrake);

        lifter_left_motor=new Spark(PWM_LIFTER_LEFT);
        lifter_right_motor=new Spark(PWM_LIFTER_RIGHT);
        lifter_left_motor.setInverted(true);
        lifter_right_motor.setInverted(false);

        feeder =new Spark(PWM_SHOOTER_FEEDER);
        conveyor=new Spark(PWM_SHOOTER_CONVEYOR);

        shooter=new CANSparkMax(CANSHOOTER,MotorType.kBrushless);
   
        shooterAngleControl=new Solenoid(PCM_SHOOTER_ANGLE);
        
        

      

    }

    
    public static void setMotorConversionLow() {
      frontLeft.getEncoder().setPositionConversionFactor(GEARSPEEDLOW);
      frontRight.getEncoder().setPositionConversionFactor(GEARSPEEDLOW);
      backLeft.getEncoder().setPositionConversionFactor(GEARSPEEDLOW);
      backRight.getEncoder().setPositionConversionFactor(GEARSPEEDLOW);
    }
    public static void setMotorConversionHigh() {
      frontLeft.getEncoder().setPositionConversionFactor(GEARSPEEDHIGH);
      frontRight.getEncoder().setPositionConversionFactor(GEARSPEEDHIGH);
      backLeft.getEncoder().setPositionConversionFactor(GEARSPEEDHIGH);
      backRight.getEncoder().setPositionConversionFactor(GEARSPEEDHIGH);
    }

  
  }

