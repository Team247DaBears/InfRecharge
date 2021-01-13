/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
//Includes hopper

import javax.lang.model.util.ElementScanner6;

//import com.revrobotics.CANEncoder;        // use Devices version
//import com.revrobotics.CANPIDController;        // use Devices version
//import com.revrobotics.CANSparkMax;        // use Devices version
import com.revrobotics.ControlType;
//import com.revrobotics.SparkMax;        // use Devices version

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {

    private double CONVEYORSPEED = -0.55;
    private double CONVEYORSHOOTSPEED = -1;
    private double FEEDER_HOLD_SPEED = -1.0;
    private double FEEDER_FEED_SPEED = 1.0;

    private SpeedController feeder;        // use Devices version
    private SpeedController conveyor;        // use Devices version


//    private CANSparkMax shooter;        // use Devices version
    //private CANEncoder encoder;
    //private CANPIDController controlLoop;

    private Solenoid shooterSolenoid;

    private double[] shooterSpeeds={-1250, -1450};
    private int shootingIndex=0;
    private boolean shooterPressed=false;
    private boolean shooterSet=false;


    //Parameteres for velocity control PID on SparkMax
    private final double KP=0.1;
    private final double KI=2e-6;
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
    private final double TARGETRPM=-1200;  //Will begin with a single setpoint.  We'll modify that for multiple distance ranges later.
    private final double CUTOFFRPM=-1190;
   

    /****************************************************************************************************************************/
    /*  Basic Autonomous ... may never be used, depending on what else becomes availalbe                                        */
    /*  The baasic autonomus sequence will be used to turn on the motor long enough tore three preloaded balls toward the target */
    /**************************************************************************************************************************** */
    private final long AUTO_SHOOT_TIME_MS=5600;//Amount of time to run the shooter for basic autonomous

    public void Init()
    {
        feeder =Devices.feeder;
        conveyor=Devices.conveyor;
        //shooter=Devices.shooterSpark;   // junit use devices version
        shooterSolenoid=Devices.shooterAngleControl;
        shooterSet=false;

        //encoder=shooter.getEncoder();
        //controlLoop=shooter.getPIDController();

        conveyor.set(0);
        Devices.shooterSpark.getPIDController().setP(KP);
        Devices.shooterSpark.getPIDController().setD(KD);
        Devices.shooterSpark.getPIDController().setI(KI);
        Devices.shooterSpark.getPIDController().setOutputRange(MINOUT, MAXOUT);
        Devices.shooterSpark.getPIDController().setIZone(IZONE);
        Devices.shooterSpark.getPIDController().setFF(FFVALUE/TARGETRPM);
        Devices.shooterSpark.getPIDController().setReference(0, ControlType.kDutyCycle);

        currentState=ShootingStates.IDLE;
        shooterPressed=false;
        shootingIndex=0;
    }

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


            SmartDashboard.putNumber("Shooting Speed", shooterSpeeds[shootingIndex]);
            SmartDashboard.putNumber("Current RPM", Devices.shooterSpark.getEncoder().getVelocity());
        
        

        
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

    public ShootingStates currentState = ShootingStates.IDLE;

    public void calcNextState()
    {
        if (currentState!=ShootingStates.IDLE)
        {
            //System.out.println("Current velocity: "+shooter.getVelocity());
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
            if (Devices.shooterSpark.getEncoder().getVelocity()<shooterSpeeds[shootingIndex]-10)
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
                shooterSet=false;
                Devices.shooterSpark.getPIDController().setReference(0, ControlType.kDutyCycle);
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
                Devices.shooterSpark.getPIDController().setReference(0, ControlType.kDutyCycle);
                conveyor.set(0);  //Will be changed if we move to something fancier
                feeder.set(0);
                break;
            }
        }


        //This is a work in progress
        public void setPID()
        {/*
            if (shooter.getVelocity()>cutoffSpeeds[shootingIndex])
            { 
                shooter.setReference(-1, ControlType.kDutyCycle);
            }
            else
            {
                shooter.setReference(0, ControlType.kDutyCycle);
            }*/
            if (!shooterSet)
            {Devices.shooterSpark.getPIDController().setReference(shooterSpeeds[shootingIndex], ControlType.kVelocity);
                shooterSet=true;
            }

        }



/*Simple autonomous.....for use in build room and until there is something better */

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
                        if (Devices.shooterSpark.getEncoder().getVelocity()<CUTOFFRPM)
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