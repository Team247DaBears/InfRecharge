/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class SemiAuto {

    public static AutoStates getAutoState() {
    if (RunSemiAuto()) { // runSemiAuto mode?
        return AutoQueue.currentQueue().autoState;
      }
      else {
        if (AutoQueue.getSize()>0) {
            AutoQueue.clearQueue();  // clear the auto queue 
            StopMotors_ResetSolenoids(); // stop all motors put silonoids to default positions
        }
      }
   return AutoStates.TeleOpt;
    }
 
    public static boolean RunSemiAuto()
    {
        if (AutoQueue.getSize()==0) { // not running semi-auto mode
            if (UserInput.getSemiAutoShooter()) {
                AutoQueue.addAutoShooterQueue(); // queue-Works in both auto and semiauto
                return true;                 
             }
            else if(UserInput.getSemiAutoIntake()) {
                AutoQueue.addIntakeQueue(); // queue-Works in both auto and semiauto
                return true;
            }
         }
         else { // Already running semi-auto mode.... must be same to continue
            AutoControlData q = AutoQueue.currentQueue();
            if (UserInput.getSemiAutoShooter()) {
                return (q.autoState == AutoStates.AutoShooter); // Same return true
            }
            else if(UserInput.getSemiAutoIntake()) {
                return (q.autoState == AutoStates.AutoIntake); // same return true
            }
        }
        return false; // all else turn off semi-auto mode
    }

 public static void StopMotors_ResetSolenoids() {
    Devices.gearShift.set(false); // set default as low

    Devices.frontLeftSpark.set(0); // set to zero speed
    Devices.frontRightSpark.set(0); // set to Zero speed
    Devices.backLeftSpark.set(0); // set to Zero speed
    Devices.backRightSpark.set(0); // set to Zero speed
    Devices.conveyor.set(0); // set to Zero speed
    Devices.feeder.set(0); // set to Zero Speed
    Devices.intake_motor.set(0); // set to Zero Speed
    Devices.shooterSpark.set(0); // set to Zero Speed
    Devices.shooterAngleControl.set(false); // Set Angle to Low
    Devices.lifter_left_motor.set(0);  // set to Zero Speed
    Devices.lifter_right_motor.set(0); // set to Zero Speed
    Devices.lifter_solenoid.set(DoubleSolenoid.Value.kReverse);  //Set default as down        
    }        
}