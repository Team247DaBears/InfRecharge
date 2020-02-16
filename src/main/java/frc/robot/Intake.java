/*----------------------------------------------------------------------------*/

/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */

/* Open Source Software - may be modified and shared by FRC teams. The code   */

/* must be accompanied by the FIRST BSD license file in the root directory of */

/* the project.                                                               */

/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;

public class Intake {
    DoubleSolenoid intakeSolenoid;

    Spark intakeMotor;
    private final double INTAKE_SPEED=1.0;

    public void Init()
    {
        intakeSolenoid=Devices.intake_solenoid;
        intakeMotor=Devices.intake_motor;
        if (intakeMotor==null)
            System.out.println("");
        else
           System.out.println("Ok.  Here I am");
    }


    public void operate()
    {
        if (UserInput.intakeRun())
        {System.out.println("I want to run");
            intakeMotor.set(INTAKE_SPEED);
            System.out.println("OK");
        }
        else 
        {
            System.out.println("I'm not running");
            intakeMotor.set(0);
            System.out.println("I didn't");
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