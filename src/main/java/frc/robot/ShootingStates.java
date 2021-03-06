/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * States for the shooter state machine.  Begin with ramp up, then start feeding
 */
public enum ShootingStates {
    IDLE,
    LOWSHOT,
    HIGHSHOT,
    RAMPING_UP,
    SHOOTING,
    FINISHED
}
