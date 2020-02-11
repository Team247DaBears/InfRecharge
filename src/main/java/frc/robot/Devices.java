/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * This class will contain all code for various devices
 * (Replaces IO)
 */
public class Devices {
    static final boolean UseSparkMax = false;
    static final boolean UseEncoder = true;
      
    private static final int CANFRONTLEFTPWM = 12;
    private static final int CANFRONTRIGHTPWM=3;
    private static final int CANBACKLEFTPWM=4;
    private static final int CANBACKRIGHTPWM=5;    

    private static final int FRONTLEFTPWM = 12; // victor
    private static final int FRONTRIGHTPWM=8; // victor
    private static final int BACKLEFTPWM=13;
    private static final int BACKRIGHTPWM=9;    

    private final static int LOWLIFTERFORWARD = 2; // solonoid1 pin0
    private final static int LOWLIFTERREVERSE = 3; // solonoid1 pin1
    private final static int HIGHLIFTERFORWARD = 4; // solonoid2 pin0
    private final static int HIGHLIFTERREVERSE = 5; // solonoid2 pin1
    private final static int LIFTERHOIST = 10; //victor

    private static final int GEARFORWARD = 6; // solonoid3 pin0
    private static final int GEARREVERSE = 7; // solonoid3 pin1

    public static DaBearsSpeedController frontLeft = null;
    public static DaBearsSpeedController frontRight = null;
    public static DaBearsSpeedController backLeft = null;
    public static DaBearsSpeedController backRight = null;

    public static DoubleSolenoid gearShift;

    //So, I'll add another set of controls, and you can comment out whichever is unused.    
      public static DoubleSolenoid lowLifter;
      public static DoubleSolenoid highLifter;
      public static DaBearsSpeedController lifterHoist;

    // Insert constants

    // Insert fields (variables, objects)

    // Insert member functions

    // Create all objects in the Init function, unless there is some reason it has
    // to wait until the beginning of autonomous.
    // This will be called from robot.init, which executes as soon as the power is
    // applied and the roborio boots up.
    public static void Init() {
      if (frontLeft==null) {
        System.out.println("Init Devics:");
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
        frontRight.setInverted(true);
        backRight.setInverted(false);

        lowLifter = new DoubleSolenoid(LOWLIFTERFORWARD, LOWLIFTERREVERSE);
        highLifter = new DoubleSolenoid(HIGHLIFTERFORWARD, HIGHLIFTERREVERSE);
        Devices.lowLifter.set(DoubleSolenoid.Value.kReverse); // set default as down
        Devices.highLifter.set(DoubleSolenoid.Value.kReverse); // set default as down
    
        lifterHoist=new DaBearsSpeedController(LIFTERHOIST, null,false);

        gearShift = new DoubleSolenoid(GEARFORWARD, GEARREVERSE);
        Devices.gearShift.set(DoubleSolenoid.Value.kReverse); // set default as low
      }
    else {
      System.out.println("Init entered twice:");
    }
    }
  }

