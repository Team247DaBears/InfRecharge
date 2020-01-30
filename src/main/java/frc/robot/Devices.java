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
import edu.wpi.first.wpilibj.Victor;

/**
 * This class will contain all code for various devices
 * (Replaces IO)
 */
public class Devices {
    private final static int LOWLIFTERFORWARD = 0;
    private final static int LOWLIFTERREVERSE = 1;
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
        lowLifter = new DoubleSolenoid(LOWLIFTERFORWARD, LOWLIFTERREVERSE);
//highLifter = new DoubleSolenoid(HIGHLIFTERFORWARD, HIGHLIFTERREVERSE);
        
    }
}
