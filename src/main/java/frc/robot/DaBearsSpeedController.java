package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANPIDController.ArbFFUnits;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

import java.lang.Math.*;

public class DaBearsSpeedController implements SpeedController {

    SpeedController speedController = null;
    SpeedController followerController = null;
    boolean useSparkMax;
    boolean useEncoder;
    Victor victorMotor = null;
    CANSparkMax sparkMaxMotor = null;
    CANEncoder sparkMaxEncoder = null;
    CANPIDController sparkMaxPIDController =null;
    Encoder victorEncoder = null;
    double currentPosition = 0;
    double Position = 0;
    double driveSpeed = 0;

    private Boolean isRunningTest = null;

    public DaBearsSpeedController(int pwm, MotorType type, boolean sparkmax, int encodepwm, int encodetype) {
        populateController(pwm, type, sparkmax);
        useEncoder = true;
        if (useSparkMax) {
            sparkMaxEncoder = sparkMaxMotor.getEncoder();
            sparkMaxPIDController = sparkMaxMotor.getPIDController();
        } else {
            victorEncoder = new Encoder(encodepwm, encodetype);
        }
    }

    /* used for Victor no Encoder */
    public DaBearsSpeedController(int pwm) {
        populateController(pwm, null, false);
        useEncoder = false;
    }

    public boolean testEncoder() {
        speedController.set(1);
        try {
            Thread.sleep(250); // wait 1/4 second to allow rotation
        } catch (InterruptedException e) {
            // Do nothing
        } 
        speedController.set(0);
        double position=0;
        if (useSparkMax) {
            position = sparkMaxEncoder.getPosition();
            if (position==0) {
                sparkMaxEncoder = null;
                sparkMaxPIDController = null;
                Thread thread = Thread.currentThread();
                DriverStation.reportError("Encoder not working for SparkMax ", thread.getStackTrace());
            }
        }
        else {
            position = victorEncoder.get();
            if (position==0) {
                victorEncoder = null;
                Thread thread = Thread.currentThread();
                DriverStation.reportError("Encoder not working for Victor ", thread.getStackTrace());
            }
        }
        return (position!=0);
    }

    public DaBearsSpeedController(int pwm, MotorType type, boolean sparkmax) {
        populateController(pwm, type, sparkmax);
    }

    public void populateController(int pwm, MotorType type, boolean sparkmax) {
        useSparkMax = sparkmax; 
        if (useSparkMax) {
            sparkMaxMotor = new CANSparkMax(pwm,type);
            speedController = sparkMaxMotor;
            useEncoder = true;
            sparkMaxEncoder = sparkMaxMotor.getEncoder();
            sparkMaxPIDController = sparkMaxMotor.getPIDController();
        }
        else {
            victorMotor =  new Victor(pwm);
            speedController =  victorMotor;
        }
    }
    public void setFollower(DaBearsSpeedController follower) {
        if (useSparkMax) {
            follower.sparkMaxMotor.follow(follower.sparkMaxMotor);
        }
        else {
            follower.speedController.set(speedController.get());
        }    
        follower.currentPosition = currentPosition;
        follower.Position = Position;
    }

    public void set(double speed) {
        if (useEncoder){
            if (useSparkMax) { 
                if (Position == 0) { // Spark Max set speed if Position = 0
                    speedController.set(speed);
                    driveSpeed = speed;
                }
            }
            else { // Victor Set Speed
                speedController.set(speed);
                driveSpeed = speed;
            }
        }
        else { // no encoder set speed
            speedController.set(speed);
            driveSpeed = speed;
        }
    }
    public double getPosition() {
        updatePosition();
        return currentPosition;
    }
    public void updatePosition() {
        double sign = java.lang.Math.signum(get());
        if (sign>=0){
            sign=1;
        }
        if (getInverted()) {
            sign = sign * -1;
            System.out.println("inverted:"+sign);
        } 
        if (useSparkMax) {
            if (sparkMaxEncoder!=null) {
                currentPosition = Position - sparkMaxEncoder.getPosition();
 //               System.out.println("sparkmax:" + currentPosition);
            }
            else {
                currentPosition = currentPosition + (1 * sign);
                //               System.out.println("decrementer:" + currentPosition);
            }
        }
        else {
            if (victorEncoder!=null) {
//                return Position - victorEncoder.get();
                if (isRunningTest()) {
                    currentPosition = currentPosition + (1 * sign);
 //                   System.out.println("decrementer:" + currentPosition);
                }
                else {
                    currentPosition = Position - victorEncoder.get();
 //                   System.out.println("victor:" + currentPosition);
                }
            }
            else {
                currentPosition = currentPosition + (1 * sign);
 //               System.out.println("decrementer:" + currentPosition);
            }
        }
    }
    public void setPosition(double position) {
        if (useSparkMax) {
            currentPosition = position;
            Position = position; // victor doesn't have setPosition
            sparkMaxEncoder.setPosition(position);
        }
        else {
            currentPosition = position;
            Position = position; // victor doesn't have setPosition
        }
    }
    public void pidWrite(double output) {
        if(useSparkMax) {
            sparkMaxMotor.pidWrite(output);
        }
        else {
            victorMotor.pidWrite(output);
        }
    }
    public void disable(){
        speedController.disable();
    }
    public double get(){
        return speedController.get();
    }
    public void setInverted(boolean inverted){
        speedController.setInverted(inverted);
    }
    public boolean getInverted(){
        return speedController.getInverted();
    }
    public void stopMotor(){
        speedController.stopMotor();
    }
    private boolean isRunningTest() {
        if (isRunningTest == null) {
            isRunningTest = true;
            try {
                Class.forName("org.junit.Test");
            } catch (ClassNotFoundException e) {
                isRunningTest = false;
            }
        }
        return isRunningTest;
    }
    public boolean close() {
        try {
            if (this.victorMotor != null) {
            this.victorMotor.close();
            this.victorMotor = null;
            this.speedController = null;
            }
        }
        catch (Exception e){
            System.err.println("Error Closing VictorMotor");
        }
        try {
            if (this.victorEncoder != null) {
                this.victorEncoder.close();
                this.victorEncoder = null;
            }
        }
        catch (Exception e){
            System.err.println("Error Closing VictorEncoder");
        }
        try {
            if (this.sparkMaxEncoder != null) {
                this.sparkMaxMotor.close();
                this.sparkMaxMotor = null;
                this.sparkMaxEncoder = null;
                this.sparkMaxPIDController = null;
                this.speedController = null;
            }
        }
        catch (Exception e){
            System.err.println("Error Closing SparkMax");
        }

            return true;
    }
    public void setP(double kp) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setP(kp);
        } 
    }
    public void setD(double kd) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setD(kd);
        } 
    }
    public void setI(double ki) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setI(ki);
        } 
    }
    public void setOutputRange(double mino, double maxo) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setOutputRange(mino,maxo);
        } 
    }
    public void setIZone(double izone) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setIZone(izone);
        } 
    }
    public void setFF(double ff) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setFF(ff);
        } 
    }
    public void setReference(double val,ControlType ct) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setReference(val, ct);
        } 
    }
    public void setReference(double val,ControlType ct, int pidslot) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setReference(val, ct, pidslot);
        } 
    }
    public void setReference(double val,ControlType ct, int pidslot, int arb) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setReference(val, ct, pidslot, arb );
        } 
    }
    public void setReference(double val,ControlType ct, int pidslot, double arbfeed, ArbFFUnits arbff) {
        if (useSparkMax && useEncoder) {
            sparkMaxPIDController.setReference(val, ct, pidslot, arbfeed, arbff );
        } 
    }
    public double getVelocity() {
        if (useSparkMax && useEncoder) {
            return sparkMaxEncoder.getVelocity();
        } 
        return this.get(); // otherwise return speed
    }

    public double restoreFactoryDefaults() {
        if (useSparkMax && useEncoder) {
            sparkMaxMotor.restoreFactoryDefaults();
        } 
        return this.get(); // otherwise return speed
    }
}