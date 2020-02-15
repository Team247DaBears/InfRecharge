/*----------------------------------------------------------------------------*/

/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */

/* Open Source Software - may be modified and shared by FRC teams. The code   */

/* must be accompanied by the FIRST BSD license file in the root directory of */

/* the project.                                                               */

/*----------------------------------------------------------------------------*/



package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;

public class Lifter
{
    private DaBearsSpeedController left_winch;
    private DaBearsSpeedController right_winch;
    private DoubleSolenoid solenoid;
    private final double WINCH_SPEED=1.0;

    public void Init() {
        left_winch=Devices.lifter_left_motor;  //This isn't very good, but I'm in a hurry
        right_winch=Devices.lifter_right_motor;
        solenoid=Devices.lifter_solenoid;
    }

    public void operate(){

        if (UserInput.getLifterUp())
        {
            solenoid.set(DoubleSolenoid.Value.kForward);
        }
        else if (UserInput.getLifterDown())
        {
            solenoid.set(DoubleSolenoid.Value.kReverse);
        }
        else
        {
            solenoid.set(DoubleSolenoid.Value.kOff);
        }

        if (UserInput.getLifterClimb())
        {
            left_winch.set(WINCH_SPEED);
            right_winch.set(WINCH_SPEED);
        }
        else
        {
            left_winch.set(0);
            right_winch.set(0);
        }
    }
}