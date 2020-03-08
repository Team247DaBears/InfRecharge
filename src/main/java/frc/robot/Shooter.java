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


import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;


public class Shooter {

    private double CONVEYORSPEED=-0.55;
    private double CONVEYORSHOOTSPEED=-1;
    private double FEEDER_HOLD_SPEED=-1.0;
    private double FEEDER_FEED_SPEED=1.0;


    private final long DEFAULT_TIME_LIMIT=6000;

    private SpeedController feeder;
    private SpeedController conveyor;


    private CANSparkMax shooter;
    private CANEncoder encoder;
    private CANPIDController controlLoop;

    private Solenoid shooterSolenoid;

    private double[] shooterSpeeds={-1250, -1450};
    private int shootingIndex=0;
    private boolean shooterSelectionPressed=false;
    private boolean shooterSet=false;//The purpose of this is to set the parameters only once per cycle.  Don't know if this is necessary


    //Parameteres for velocity control PID on SparkMax
    //The parameters used at Southfield are adequate, but should be improved upon.
    private final double KP=0.1;
    private final double KI=0;
    private final double KD=0;
    private final double MAXOUT=0;
    private final double MINOUT=-1; //Never run backwards
                                //It would be logical to use setInverted, and just use positive integers.
                                //As logical as that would be, I'm not going to do it immediately, because
                                //I have it from others that the encoders are inverted, and that might not work
                                //with this.  The solution would be to modify DaBearsSpeedController to invert the encoder
                                //if using setInverted, but that will require some testing.
    private final double FFVALUE=-0;  //Will require experimentation to set a better value
    private final double IZONE=0;

   
    private long shootingTimeLimit=DEFAULT_TIME_LIMIT;  //The maximum amount of time allowed for a shooting sequence, beginning at ramp u
                                     //May be set extermally to allow different values in different autonomous modes
    private long shootingStartTime;

    private double targetRPM;



    public void Init()
    {
        feeder =Devices.feeder;
        conveyor=Devices.conveyor;
        shooter=Devices.shooter;
        shooterSolenoid=Devices.shooterAngleControl;
        shooterSet=false;

        encoder=shooter.getEncoder();
        controlLoop=shooter.getPIDController();

        conveyor.set(0);
        controlLoop.setP(KP);
        controlLoop.setD(KD);
        controlLoop.setI(KI);
        controlLoop.setOutputRange(MINOUT, MAXOUT);
        controlLoop.setIZone(IZONE);
        controlLoop.setFF(FFVALUE);
        controlLoop.setReference(0, ControlType.kDutyCycle);

        currentState=ShootingStates.IDLE;
        shooterSelectionPressed=false;//The usual "is the button still held down" state machine
        shootingIndex=0;
    }

    public void teleopInit()
    {
        currentState=ShootingStates.IDLE;
        shootingIndex=0;
        shooterSelectionPressed=false;
        shootingIndex=0;
        targetRPM=shooterSpeeds[shootingIndex];

    }


    /**
     * Execute shooter operations in teleoperated mode
     */
    public void operate()
    {
        //Pick shooting speed
        if ((!shooterSelectionPressed) && (UserInput.getShooterSpeedIncrement()))
            {
                shooterSelectionPressed=true;
                shootingIndex++;
                if (shootingIndex>=shooterSpeeds.length)
                {
                    shootingIndex=0;
                    
                }
                targetRPM=shooterSpeeds[shootingIndex];
            }
        else if (!UserInput.getShooterSpeedIncrement())
            {
                shooterSelectionPressed=false;
            }



        //Set the angle for the shooter
        if (UserInput.getShooterLowShot())
        {
            shooterSolenoid.set(true);
        }
        else
        {
            shooterSolenoid.set(false);
        }



        //Either begin or end shooting, based on user input
        if (!UserInput.getShooting())
        {
            resetShooting();
        }
        else if ((currentState==ShootingStates.IDLE)&&(UserInput.getShooting()))
        {
            commenceShooting();
        }

        //React to request for feed
        {
            if (UserInput.intakeRun())
            {
                setConveyor(true);
            }
            else
            {
                setConveyor(false);
            }
        }


        execute();
    }

    public ShootingStates currentState = ShootingStates.IDLE;

    public void calcNextState()
    {
        long now=System.currentTimeMillis();
        switch(currentState)
        {
            case IDLE:
            break;
            case PICKINGUP:
            break;
            case RAMPING_UP:
            if (encoder.getVelocity()<targetRPM+10)  //Remember negative
            {
                currentState=ShootingStates.SHOOTING;
            }
            else if ((now-shootingStartTime)>shootingTimeLimit)
            {
                currentState=ShootingStates.IDLE; //Abort if cannot get up to speed
            }
            break;
            case SHOOTING:
            if (now-shootingStartTime>shootingTimeLimit) 
                currentState=ShootingStates.FINISHED;
            break;
            case FINISHED:
            break;

        }
    }

        public void setOutputs()
        {
            switch(currentState)
            {
                case IDLE:
                shooterSet=false;
                controlLoop.setReference(0, ControlType.kDutyCycle);
                conveyor.set(0);
                feeder.set(FEEDER_HOLD_SPEED);
                break;
                case PICKINGUP:
                shooterSet=false;
                controlLoop.setReference(0, ControlType.kDutyCycle);
                conveyor.set(CONVEYORSPEED);
                feeder.set(FEEDER_HOLD_SPEED);
                break;
                case RAMPING_UP:
                setPID();
                conveyor.set(CONVEYORSPEED);
                feeder.set(FEEDER_HOLD_SPEED);
                break;
                case SHOOTING:
                setPID();
                conveyor.set(CONVEYORSHOOTSPEED);
                feeder.set(FEEDER_FEED_SPEED);
                break;
                case FINISHED:
                controlLoop.setReference(0, ControlType.kDutyCycle);
                conveyor.set(0);  //Will be changed if we move to something fancier
                feeder.set(0);
                break;
            }
        }


        /**
         * Put shooter back in state ready to accept another shooting command
         */
        public void resetShooting()
        {
            currentState=ShootingStates.IDLE;
            shooterSet=false;
        }

        public void completeShooting()
        {
            currentState=ShootingStates.FINISHED;
        }

        public void commenceShooting()
        {
            currentState=ShootingStates.RAMPING_UP;
            shootingStartTime=System.currentTimeMillis();
        }

        public void setTimeLimit(long timeLimit)
        {
            shootingTimeLimit=timeLimit;
        }

        public void setConveyor(boolean isPicking)
        {
            if (isPicking)
                if (currentState==ShootingStates.IDLE)
                {
                    currentState=ShootingStates.PICKINGUP;
                }
            else
            {
                if (currentState==ShootingStates.PICKINGUP)
                {
                    currentState=ShootingStates.IDLE;
                }
            }
        }

        public void setTargetRPM(double target)
        {
            targetRPM=target;
        }




        /**
         * Based on your current state, continue on
         */
        public void execute()
        {
            calcNextState();
            setOutputs();
        }


        //This is a work in progress
        public void setPID()
        {
            if (!shooterSet)
            {controlLoop.setReference(targetRPM, ControlType.kVelocity);
                shooterSet=true;
            }

        }




        public boolean isAutoShootComplete()
        {
            return currentState==ShootingStates.FINISHED;
        }


    


}