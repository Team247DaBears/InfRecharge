/*----------------------------------------------------------------------------*/

/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */

/* Open Source Software - may be modified and shared by FRC teams. The code   */

/* must be accompanied by the FIRST BSD license file in the root directory of */

/* the project.                                                               */

/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class Intake {
    DoubleSolenoid intakeSolenoid;
    private double CONVEYORSPEED = -0.60;

    SpeedController intakeMotor;
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
    public void AutoIntake(){
        if (AutoQueue.getSize() ==0) {return;}
        AutoControlData q = AutoQueue.currentQueue();
        switch (q.intakeState) {
            case intakeRun:
                Devices.gearShift.set(false); // set low speed
                Devices.setMotorConversionLow();           

                Devices.conveyor.set(CONVEYORSPEED);
                intakeMotor.set(INTAKE_SPEED);
                q.intakeState = IntakeStates.intakeDown;

                InitEncoderController(Devices.frontLeftSpark);
                InitEncoderController(Devices.frontRightSpark);
                InitEncoderController(Devices.backLeftSpark);
                InitEncoderController(Devices.backRightSpark);        
            break;
            case intakeDown:
//                intakeSolenoid.set(DoubleSolenoid.Value.kForward);
                double frontLeftPos = Devices.frontLeftEncoder.getPosition();
                double frontRightPos = Devices.frontRightEncoder.getPosition();

                double leftDiff = java.lang.Math.abs(intakeTravel - frontLeftPos);
                double rightDiff = java.lang.Math.abs(intakeTravel - frontRightPos);
                //System.out.println("diff:"+leftDiff);
                //System.out.println("diff:"+rightDiff);
                if (leftDiff > .2 || rightDiff > .2) {
                    System.out.println("IntakeTravel" + intakeTravel);
                    Devices.frontLeftPID.setReference(intakeTravel, ControlType.kPosition);
                    Devices.frontRightPID.setReference(intakeTravel, ControlType.kPosition);
                    Devices.backLeftPID.setReference(intakeTravel, ControlType.kPosition);
                    Devices.backRightPID.setReference(intakeTravel, ControlType.kPosition);
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
     public static void InitEncoderController(CANSparkMax motor) {
        //motor.restoreFactoryDefaults();
        motor.set(0);
        motor.getPIDController().setP(KP);
        motor.getPIDController().setD(KD);
        motor.getPIDController().setI(KI);
        motor.getPIDController().setOutputRange(MINOUT, MAXOUT);
        motor.getPIDController().setIZone(IZONE);
        motor.getPIDController().setFF(FFVALUE/TARGETRPM);
        motor.set(0);
        motor.getEncoder().setPosition(0);
    }
}