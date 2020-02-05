package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder.*;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public  class DaBearsSpeedController implements SpeedController {
    SpeedController speedController = null;
    SpeedController followerController = null;
    boolean useSparkMax;
    boolean useEncoder;
    Victor victorMotor = null;
    CANSparkMax sparkMaxMotor = null;
    CANEncoder sparkMaxEncoder = null;
    Encoder victorEncoder = null;
    double currentPosition = 0;
    double Position = 0;

    private Boolean isRunningTest = null;

    public DaBearsSpeedController(int pwm, MotorType type, boolean sparkmax, int encodepwm,int encodetype) {
        populateController(pwm, type, sparkmax);
        useEncoder = true;
        if (useSparkMax) {
            sparkMaxEncoder = new CANEncoder((CANSparkMax)sparkMaxMotor);    
        }
        else {
            victorEncoder = new Encoder(encodepwm, encodetype);
        }
    }

    public DaBearsSpeedController(int pwm, MotorType type, boolean sparkmax) {
        populateController(pwm, type, sparkmax);
    }

    public void populateController(int pwm, MotorType type, boolean sparkmax) {
        useSparkMax = sparkmax; 
        if (useSparkMax) {
            sparkMaxMotor = new CANSparkMax(pwm,type);
            speedController = sparkMaxMotor;
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
                }
            }
            else { // Victor Set Speed
                speedController.set(speed);
            }
        }
        else { // no encoder set speed
            speedController.set(speed);
        }
    }
    public double getPosition() {
        return currentPosition;
    }
    public void updatePosition() {
        if (useSparkMax) {
            if (sparkMaxEncoder!=null) {
                currentPosition = Position - sparkMaxEncoder.getPosition();
                System.out.println("sparkmax:" + currentPosition);
            }
            else {
                currentPosition--;
                System.out.println("decrementer:" + currentPosition);
            }
        }
        else {
            if (victorEncoder!=null) {
//                return Position - victorEncoder.get();
                if (isRunningTest()) {
                    currentPosition--;
                    System.out.println("decrementer:" + currentPosition);
                }
                else {
                    currentPosition = Position - victorEncoder.get();
                    System.out.println("victor:" + currentPosition);
                }
            }
            else {
                currentPosition--;
                System.out.println("decrementer:" + currentPosition);
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
}