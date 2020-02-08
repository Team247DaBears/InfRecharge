/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;//
//import jdk.internal.jline.console.UserInterruptException;

/**
 * Add your docs here.
 */
public class Intake {

    DoubleSolenoid intakeSolenoid;
    Talon intakeMotor;

    private final double INTAKE_SPEED=1.0;

    private final int INTAKE_PORT_FORWARD=0;
    private final int INTAKE_PORT_BACK=1;

    private final int INTAKE_MOTOR_PWM=0;

    public void Init()
    {
        intakeSolenoid=new DoubleSolenoid(INTAKE_PORT_FORWARD,INTAKE_PORT_BACK);
        intakeMotor=new Talon(INTAKE_MOTOR_PWM);
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
}}
