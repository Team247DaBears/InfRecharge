/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

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

    private static final int FRONTLEFTPWM = 1;
    private static final int FRONTRIGHTPWM=8;
    private static final int BACKLEFTPWM=0;
    private static final int BACKRIGHTPWM=9;    

    private final static int LOWLIFTERFORWARD = 2;
    private final static int LOWLIFTERREVERSE = 3;
    private final static int HIGHLIFTERFORWARD = 4;
    private final static int HIGHLIFTERREVERSE = 5;
    private final static int LIFTERHOIST = 10;

    private static final int GEARFORWARD = 6;
    private static final int GEARREVERSE = 7;

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
          frontLeft=new DaBearsSpeedController(CANFRONTLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,12,13);
          frontRight=new DaBearsSpeedController(CANBACKLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,14,15);

          backLeft=new DaBearsSpeedController(CANFRONTRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,16,17);
          backRight=new DaBearsSpeedController(CANBACKRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,18,19);
        }
        else {
          frontLeft=new DaBearsSpeedController(FRONTLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,12,13);
          frontRight=new DaBearsSpeedController(BACKLEFTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,14,15);

          backLeft=new DaBearsSpeedController(FRONTRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,16,17);
          backRight=new DaBearsSpeedController(BACKRIGHTPWM, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless,UseSparkMax,18,19);
        }
        frontRight.setInverted(true);
        backRight.setInverted(false);

        lowLifter = new DoubleSolenoid(LOWLIFTERFORWARD, LOWLIFTERREVERSE);
        highLifter = new DoubleSolenoid(HIGHLIFTERFORWARD, HIGHLIFTERREVERSE);
        lifterHoist=new DaBearsSpeedController(LIFTERHOIST, null,false);

        gearShift = new DoubleSolenoid(GEARFORWARD, GEARREVERSE);
      }
    else {
      System.out.println("Init entered twice:");
    }
    }
  }

