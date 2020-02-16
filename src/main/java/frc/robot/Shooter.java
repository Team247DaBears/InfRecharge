/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
//Includes hopper

import javax.lang.model.util.ElementScanner6;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.SparkMax;

import edu.wpi.first.wpilibj.Solenoid;

public class Shooter {

    private double CONVEYORSPEED=-0.40;
    private double CONVEYORSHOOTSPEED=-1;
    private double FEEDER_HOLD_SPEED=-1.0;
    private double FEEDER_FEED_SPEED=1.0;

    private DaBearsSpeedController feeder;
    private DaBearsSpeedController conveyor;


    private CANSparkMax shooter;
    private CANEncoder encoder;
    private CANPIDController controlLoop;

    private Solenoid shooterSolenoid;


    //Parameteres for velocity control PID on SparkMax
    private final double KP=5e-5;
    private final double KI=2e-6;
    private final double KD=0;
    private final double MAXOUT=1;
    private final double MINOUT=-1; //Never run backwards
                                //It would be logical to use setInverted, and just use positive integers.
                                //As logical as that would be, I'm not going to do it immediately, because
                                //I have it from others that the encoders are inverted, and that might not work
                                //with this.  The solution would be to modify DaBearsSpeedController to invert the encoder
                                //if using setInverted, but that will require some testing.
    private final double FFVALUE=-0.22;  //Will require experimentation to set a better value
    private final double IZONE=200;
    private final double TARGETRPM=-1000;  //Will begin with a single setpoint.  We'll modify that for multiple distance ranges later.


    public void Init()
    {
        feeder =Devices.feeder;
        conveyor=Devices.conveyor;
        shooter=Devices.shooter;
        shooterSolenoid=Devices.shooterAngleControl;

        encoder=shooter.getEncoder();
        controlLoop=shooter.getPIDController();


        conveyor.set(CONVEYORSPEED);
        controlLoop.setP(KP);
        controlLoop.setD(KD);
        controlLoop.setI(KI);
        controlLoop.setOutputRange(MINOUT, MAXOUT);
        controlLoop.setIZone(IZONE);
        controlLoop.setFF(FFVALUE/TARGETRPM);
        controlLoop.setReference(0, ControlType.kDutyCycle);

        currentState=ShootingStates.IDLE;
    }

    public void operate()
    {

        if (UserInput.getShooterLowShot())
        {
            shooterSolenoid.set(true);
        }
        else
        {
            shooterSolenoid.set(false);
        }

        calcNextState();
        setOutputs();
    }


    public ShootingStates currentState;

    public void calcNextState()
    {
        if (currentState!=ShootingStates.IDLE)
        {
            System.out.println("Current velocity: "+encoder.getVelocity());
        }
        switch(currentState)
        {
            case IDLE:
            if (UserInput.getShooting())
            {
                currentState=ShootingStates.RAMPING_UP;
                
            }
            break;
            case RAMPING_UP:
            if (!UserInput.getShooting())
            {
                currentState=ShootingStates.IDLE;
            }

            if (Math.abs((encoder.getVelocity()-TARGETRPM)/TARGETRPM)<=.05)
            {
                currentState=ShootingStates.SHOOTING;
            }
            break;
            case SHOOTING:
            if (!UserInput.getShooting())
            {
                currentState=ShootingStates.IDLE;
            }
            break;

        }
    }

        public void setOutputs()
        {
            switch(currentState)
            {
                case IDLE:
                controlLoop.setReference(0, ControlType.kDutyCycle);
                conveyor.set(CONVEYORSPEED);
                feeder.set(FEEDER_HOLD_SPEED);
                break;
                case RAMPING_UP:
                //controlLoop.setReference(TARGETRPM, ControlType.kVelocity);
                //controlLoop.setReference(UserInput.getMotorSpeed(),ControlType.kDutyCycle);
                setPID();
                conveyor.set(CONVEYORSPEED);
                feeder.set(FEEDER_HOLD_SPEED);
                break;
                case SHOOTING:
                //controlLoop.setReference(TARGETRPM, ControlType.kVelocity);
                //controlLoop.setReference(UserInput.getMotorSpeed(), ControlType.kDutyCycle);
                setPID();
                conveyor.set(CONVEYORSHOOTSPEED);
                feeder.set(FEEDER_FEED_SPEED);
                break;
            }
        }


        //This is a work in progress
        public void setPID()
        {
            if (encoder.getVelocity()>-900)
            { 
                controlLoop.setReference(-1, ControlType.kDutyCycle);
            }
            else if (encoder.getVelocity()<-1050)
            {controlLoop.setReference(+1, ControlType.kDutyCycle);}
            else
            {
                controlLoop.setReference(-1000, ControlType.kVelocity);
            }
        }
    }



