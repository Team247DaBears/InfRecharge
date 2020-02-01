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
    private static final int GEARFORWARD = 1;
    private static final int GEARREVERSE = 0;
    
    private static final int FRONTLEFTPWM = 1;
      private static final int FRONTRIGHTPWM=2;
      private static final int BACKLEFTPWM=0;
      private static final int BACKRIGHTPWM=3;
      private static final int LEFTROLLER=4;
      private static final int RIGHTROLLER=5;
  
  
      public static SpeedController frontLeft;
      public static SpeedController frontRight;
      public static SpeedController backLeft;
      public static SpeedController backRight;
      public static DoubleSolenoid gearShift;
   
      //So, I'll add another set of controls, and you can comment out whichever is unused.
      public static SpeedController leftRollerMotor;
      public static SpeedController rightRollerMotor;
    private final static int LOWLIFTERFORWARD = 2;
    private final static int LOWLIFTERREVERSE = 3;
   //private final static int HIGHLIFTERFORWARD = 1;
  //private final static int HIGHLIFTERREVERSE = 1;

    public static DoubleSolenoid lowLifter;
    public static DoubleSolenoid highLifter;

    // Insert constants

    // Insert fields (variables, objects)

    // Insert member functions

    // Create all objects in the Init function, unless there is some reason it has
    // to wait until the beginning of autonomous.
    // This will be called from robot.init, which executes as soon as the power is
    // applied and the roborio boots up.
    public static void Init() {
        
System.out.println("Devices");
leftRollerMotor=new Victor(LEFTROLLER);
rightRollerMotor=new Victor(RIGHTROLLER);
frontLeft=new Victor(FRONTLEFTPWM);

frontRight=new Victor(FRONTRIGHTPWM);
backLeft=new Victor(BACKLEFTPWM);

backRight=new Victor(BACKRIGHTPWM);

frontRight.setInverted(true);
backRight.setInverted(true);

 gearShift = new DoubleSolenoid(GEARFORWARD, GEARREVERSE);
 lowLifter = new DoubleSolenoid(LOWLIFTERFORWARD, LOWLIFTERREVERSE);
//highLifter = new DoubleSolenoid(HIGHLIFTERFORWARD, HIGHLIFTERREVERSE);
}




    }

