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


    private DaBearsSpeedController shooter;
    //private CANEncoder encoder;
    //private CANPIDController controlLoop;

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
    private final double TARGETRPM=-1100;  //Will begin with a single setpoint.  We'll modify that for multiple distance ranges later.
    private final double CUTOFFRPM=-1090;
    private final double FIRINGTHRESHOLD=-950; //Begin feeding balls when this threshold is reached


    /****************************************************************************************************************************/
    /*  Basic Autonomous ... may never be used, depending on what else becomes availalbe                                        */
    /*  The baasic autonomus sequence will be used to turn on the motor long enough tore three preloaded balls toward the target */
    /**************************************************************************************************************************** */
    private final long AUTO_SHOOT_TIME_MS=9000;//Amount of time to run the shooter for basic autonomous


    public void Init()
    {
        feeder =Devices.feeder;
        conveyor=Devices.conveyor;
        shooter=Devices.shooter;
        shooterSolenoid=Devices.shooterAngleControl;

        //encoder=shooter.getEncoder();
        //controlLoop=shooter.getPIDController();

        

        



        conveyor.set(CONVEYORSPEED);
        shooter.setP(KP);
        shooter.setD(KD);
        shooter.setI(KI);
        shooter.setOutputRange(MINOUT, MAXOUT);
        shooter.setIZone(IZONE);
        shooter.setFF(FFVALUE/TARGETRPM);
        shooter.setReference(0, ControlType.kDutyCycle);

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
            System.out.println("Current velocity: "+shooter.getVelocity());
        }
        switch(currentState)
        {
            case HIGHSHOT:
            case LOWSHOT:
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

 //if (Math.abs((encoder.getVelocity()-TARGETRPM)/TARGETRPM)<=.05)
 //           {
 //               currentState=ShootingStates.SHOOTING;
 //           }
            if (encoder.getVelocity()<CUTOFFRPM)
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
            case FINISHED:
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
                case HIGHSHOT:
                case LOWSHOT:
                case IDLE:
                shooter.setReference(0, ControlType.kDutyCycle);
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
                case FINISHED:
                controlLoop.setReference(0, ControlType.kDutyCycle);
                conveyor.set(0);  //Will be changed if we move to something fancier
                feeder.set(0);
                break;
            }
        }


        //This is a work in progress
        public void setPID()
        {
            if (encoder.getVelocity()>CUTOFFRPM)
            { 
                shooter.setReference(-1, ControlType.kDutyCycle);
            }
            else
            {
                controlLoop.setReference(0, ControlType.kDutyCycle);
            }
            /*
            else if (encoder.getVelocity()<-1050)
            {controlLoop.setReference(+1, ControlType.kDutyCycle);}
            else
            {
                controlLoop.setReference(-1000, ControlType.kVelocity);
            }*/
        }

        public boolean isAutoShootComplete()
        {
            return currentState==ShootingStates.FINISHED;
        }


        public long autoBeginTime=0;
        public void calcNextStateAuto()
        {
            long elapsed;
            switch(currentState)
            {
                case IDLE:
                      autoBeginTime=System.currentTimeMillis();
                      currentState=ShootingStates.RAMPING_UP;
                      break;
                case RAMPING_UP:
                        elapsed=System.currentTimeMillis()-autoBeginTime;
                      if (elapsed>AUTO_SHOOT_TIME_MS)
                        currentState=ShootingStates.FINISHED;
                      else if (encoder.getVelocity()<FIRINGTHRESHOLD)
                      {
                          currentState=ShootingStates.SHOOTING;
                      }
                      break;
                case SHOOTING:
                elapsed=System.currentTimeMillis()-autoBeginTime;
                      if (elapsed>AUTO_SHOOT_TIME_MS)
                      {
                          currentState=ShootingStates.FINISHED;
                      }
                      break;
                default:
                     currentState=ShootingStates.FINISHED;
                     break;
            }
        }

        public boolean isIntakeRunning()
        {
            return false;  //Needs some experimentation, but the idea is that during intake running, the conveyor might move faster.
        }
    }


    public void AutoShoot() {
        AutoControlData q = AutoQueue.currentQueue();
        switch (q.shootingState) {
            case HIGHSHOT:
                q.shootingState=ShootingStates.RAMPING_UP;
                shooterSolenoid.set(true);
                break;
            case LOWSHOT:
                q.shootingState=ShootingStates.RAMPING_UP;
                shooterSolenoid.set(false);
                break;
            case IDLE:
                AutoQueue.removeCurrent();
                break;
            case RAMPING_UP:
                if (Math.abs((shooter.getVelocity()-TARGETRPM)/TARGETRPM)<=.05)
                {
                    q.shootingState=ShootingStates.SHOOTING;
                }
                break;
            case SHOOTING:
                if (q.shootingRamp <= 0.0)
                {
                    AutoQueue.removeCurrent();
                }
                break;
            }
        currentState = q.shootingState;
        q.shootingRamp--;
        setOutputs();
    }

}