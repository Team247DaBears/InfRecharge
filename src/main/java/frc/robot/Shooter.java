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
    private boolean shooterPressed=false;
    private boolean shooterSet=false;


    //Parameteres for velocity control PID on SparkMax
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
    private final double IZONE=200;

   
    private long shootingTimeLimit=DEFAULT_TIME_LIMIT;  //The maximum amount of time allowed for a shooting sequence, beginning at ramp u
                                     //May be set extermally to allow different values in different autonomous modes
    private long shootingStartTime;



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
        shooterPressed=false;
        shootingIndex=0;
    }

    public void teleopInit()
    {
        currentState=ShootingStates.IDLE;
    }


    /**
     * Execute shooter operations in teleoperated mode
     */
    public void operate()
    {
 
        if ((!shooterPressed) && (UserInput.getShooterSpeedIncrement()))
            {
                shooterPressed=true;
                shootingIndex++;
                if (shootingIndex>=shooterSpeeds.length)
                {
                    shootingIndex=0;
                }
            }
        else if (!UserInput.getShooterSpeedIncrement())
            {
                shooterPressed=false;
            }



        
        if (UserInput.getShooterLowShot())
        {
            shooterSolenoid.set(true);
        }
        else
        {
            shooterSolenoid.set(false);
        }

        if (!UserInput.getShooting())
        {
            resetShooting();
        }
        else if ((currentState!=ShootingStates.IDLE)&&(UserInput.getShooting()))
        {
            commenceShooting();
        }



        calcNextState();
        setOutputs();
    }

    public ShootingStates currentState = ShootingStates.IDLE;

    public void calcNextState()
    {
        long now=System.currentTimeMillis();
        switch(currentState)
        {
            case IDLE:
            break;
            case RAMPING_UP:
            if (encoder.getVelocity()<shooterSpeeds[shootingIndex]+10)
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
                if (UserInput.intakeRun())
                {conveyor.set(CONVEYORSPEED);
                }
                else
                {
                    conveyor.set(0);
                }
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
            {controlLoop.setReference(shooterSpeeds[shootingIndex], ControlType.kVelocity);
                shooterSet=true;
            }

        }



/*Simple autonomous.....for use in build room and until there is something better */

        public boolean isAutoShootComplete()
        {
            return currentState==ShootingStates.FINISHED;
        }



        public boolean isIntakeRunning()
        {
            return false;  //Needs some experimentation, but the idea is that during intake running, the conveyor might move faster.
        }
    


}