/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This includes all code that interacts with the driver station via joysticks or other devices
 */
public class DriveStation {

    private static final int FIRSTJOYSTICK_PORT=0;

    private static Joystick firstJoystick;  //I'm putting this in to emphasize that the joystick object is PRIVATE

    //Include initialization code here.  This is called by RobotInit() which executes as soon as power is applied and the RoboRio boots up
    public  static void Init()
    {
        firstJoystick=new Joystick(FIRSTJOYSTICK_PORT);
    }
}
