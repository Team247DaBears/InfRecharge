/*----------------------------------------------------------------------------*/

/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */

/* Open Source Software - may be modified and shared by FRC teams. The code   */

/* must be accompanied by the FIRST BSD license file in the root directory of */

/* the project.                                                               */

/*----------------------------------------------------------------------------*/



package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Intake {
    DoubleSolenoid intakeSolenoid;

    DaBearsSpeedController intakeMotor;
    private final double INTAKE_SPEED=1.0;

    public void Init()
    {
        intakeSolenoid=Devices.intake_solenoid;
        intakeMotor=Devices.intake_motor;
    }

    public void operate()
    {
        if (UserInput.intakeRun())
        {
            intakeMotor.set(INTAKE_SPEED);
        }
        else 
        {
            intakeMotor.set(0);
        }

        if (UserInput.intakeDown())
        {
            intakeSolenoid.set(DoubleSolenoid.Value.kForward);
        }

        else if (UserInput.intakeUp())
        {
            intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
        else 
       {
           intakeSolenoid.set(DoubleSolenoid.Value.kOff);
        }
}
}