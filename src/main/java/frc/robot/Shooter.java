/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
//Includes hopper

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.SparkMax;

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


    //Parameteres for velocity control PID on SparkMax
    private final double KP=5e-5;
    private final double KI=0;
    private final double KD=0;
    private final double MAXOUT=0;
    private final double MINOUT=-1; //Never run backwards
                                //It would be logical to use setInverted, and just use positive integers.
                                //As logical as that would be, I'm not going to do it immediately, because
                                //I have it from others that the encoders are inverted, and that might not work
                                //with this.  The solution would be to modify DaBearsSpeedController to invert the encoder
                                //if using setInverted, but that will require some testing.
    private final double FFVALUE=0.0;  //Will require experimentation to set a better value
    private final double TARGETRPM=-1000;  //Will begin with a single setpoint.  We'll modify that for multiple distance ranges later.


    public void Init()
    {
        feeder =Devices.feeder;
        conveyor=Devices.conveyor;
        shooter=Devices.shooter;

        encoder=shooter.getEncoder();
        controlLoop=shooter.getPIDController();


        conveyor.set(CONVEYORSPEED);
        controlLoop.setP(KP);
        controlLoop.setD(KD);
        controlLoop.setI(KI);
        controlLoop.setOutputRange(MINOUT, MAXOUT);
        controlLoop.setReference(0, ControlType.kDutyCycle);

        currentState=ShootingStates.IDLE;
    }

    public void operate()
    {
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
            if (UserInput.getFeeding())
            {
                currentState=ShootingStates.RAMPING_UP;
                
            }
            break;
            case RAMPING_UP:
            if (!UserInput.getFeeding())
            {
                currentState=ShootingStates.IDLE;
            }

            if (Math.abs((encoder.getVelocity()-TARGETRPM)/TARGETRPM)<=.05)
            {
                currentState=ShootingStates.FEEDING;
            }
            break;
            case FEEDING:
            if (!UserInput.getFeeding())
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
                controlLoop.setReference(TARGETRPM, ControlType.kVelocity);
                conveyor.set(CONVEYORSPEED);
                feeder.set(FEEDER_HOLD_SPEED);
                break;
                case FEEDING:
                controlLoop.setReference(TARGETRPM, ControlType.kVelocity);
                conveyor.set(CONVEYORSHOOTSPEED);
                feeder.set(FEEDER_FEED_SPEED);
                break;
            }
        }
    }



