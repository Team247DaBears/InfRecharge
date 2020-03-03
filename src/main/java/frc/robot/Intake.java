/*----------------------------------------------------------------------------*/

/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */

/* Open Source Software - may be modified and shared by FRC teams. The code   */

/* must be accompanied by the FIRST BSD license file in the root directory of */

/* the project.                                                               */

/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;

public class Intake {
    DoubleSolenoid intakeSolenoid;

    SpeedController intakeMotor;
    IntakeStates intakeState;
    private final double INTAKE_SPEED=.5;




    public void Init()
    {
        Devices.conveyor.set(0); // turn off conveyor
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