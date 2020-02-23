/*----------------------------------------------------------------------------*/

/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */

/* Open Source Software - may be modified and shared by FRC teams. The code   */

/* must be accompanied by the FIRST BSD license file in the root directory of */

/* the project.                                                               */

/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;

public class Intake {
    DoubleSolenoid intakeSolenoid;

    DaBearsSpeedController intakeMotor;
    IntakeStates intakeState;
    private final double INTAKE_SPEED=.5;
    private final int intakeTravel = 16; // go distance of ball size plus 4 in. 

    private static final double KP=5e-5;
    private static final double KI=2e-6;
    private static final double KD=0;
    private static final double MAXOUT=.5; // during intake drive speed
    private static final double MINOUT=-.5;
    private static final double FFVALUE=-0.22;  //Will require experimentation to set a better value
    private static final double IZONE=200;
    private static final double TARGETRPM=-1000;  //Will begin with a single setpoint.  We'll modify that for multiple distance ranges later.

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
    public void AutoIntake(){
        AutoControlData q = AutoQueue.currentQueue();
        switch (q.intakeState) {
            case intakeRun:
                Devices.gearShift.set(false); // set low speed
                Devices.setMotorConversionLow();           

                intakeMotor.set(INTAKE_SPEED);
                q.intakeState = IntakeStates.intakeDown;

                InitEncoderController(Devices.frontLeft);
                InitEncoderController(Devices.frontRight);
                InitEncoderController(Devices.backLeft);
                InitEncoderController(Devices.backRight);        
            break;
            case intakeDown:
//                intakeSolenoid.set(DoubleSolenoid.Value.kForward);
                double frontLeftPos = Devices.frontLeft.getPosition();
                double frontRightPos = Devices.frontRight.getPosition();

                double leftDiff = java.lang.Math.abs(intakeTravel - frontLeftPos);
                double rightDiff = java.lang.Math.abs(intakeTravel - frontRightPos);
                System.out.println("diff:"+leftDiff);
                System.out.println("diff:"+rightDiff);
                if (leftDiff > .2 || rightDiff > .2) {
                    System.out.println("IntakeTravel" + intakeTravel);
                    Devices.frontLeft.setReference(intakeTravel, ControlType.kPosition);
                    Devices.frontRight.setReference(intakeTravel, ControlType.kPosition);
                    Devices.backLeft.setReference(intakeTravel, ControlType.kPosition);
                    Devices.backRight.setReference(intakeTravel, ControlType.kPosition);
                }
                else {
                    q.intakeState = IntakeStates.intakeUp;
                }
                break;
            case intakeUp:
                intakeMotor.set(0);
                q.intakeState = IntakeStates.intakeStop;
            //                intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
            break;
            case intakeStop:
                intakeSolenoid.set(DoubleSolenoid.Value.kOff);
                AutoQueue.removeCurrent();
                break;    
        }
     }
     public static void InitEncoderController(DaBearsSpeedController motor) {
        //motor.restoreFactoryDefaults();
        motor.set(0);
        motor.setP(KP);
        motor.setD(KD);
        motor.setI(KI);
        motor.setOutputRange(MINOUT, MAXOUT);
        motor.setIZone(IZONE);
        motor.setFF(FFVALUE/TARGETRPM);
        motor.set(0);
        motor.setPosition(0);
    }
}