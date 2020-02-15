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

    DoubleSolenoid intakeSolenoid = null;
    Talon intakeMotor;

    private static final double INTAKE_SPEED=1.0;

    private static final int INTAKE_PORT_FORWARD=1;
    private static final int INTAKE_PORT_BACK=0;

    private static final int INTAKE_MOTOR_PWM=19;

    public IntakeStates intakeStateMotor = null;
    public IntakeStates intakeStateArms = null;

    public void Init()
    {

        if (intakeSolenoid == null) {
            System.out.println("Intake.Init()");
            intakeSolenoid=new DoubleSolenoid(INTAKE_PORT_FORWARD,INTAKE_PORT_BACK);
            intakeMotor=new Talon(INTAKE_MOTOR_PWM);
        }
        else {
            System.out.println("Intake.Init() more than once");
        }

    }


    public void operate()
    {
        if (UserInput.intakeRun())
        {
            this.intakeStateMotor = IntakeStates.intakeRun;
            intakeMotor.set(INTAKE_SPEED);
        }
        else 
        {
            intakeMotor.set(0);
            this.intakeStateMotor = IntakeStates.intakeStop;
        }

        if (UserInput.intakeDown())
        {
            intakeSolenoid.set(DoubleSolenoid.Value.kForward);
            intakeStateArms = IntakeStates.intakeDown;
        }
        else if (UserInput.intakeUp())
        {
            intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
            intakeStateArms = IntakeStates.intakeUp;
        }
        else 
       {
           intakeSolenoid.set(DoubleSolenoid.Value.kOff);
           intakeStateArms = IntakeStates.intakeStop;
        }
    }
}
