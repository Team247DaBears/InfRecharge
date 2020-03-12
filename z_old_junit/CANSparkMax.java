package frc.robot;

import java.util.concurrent.atomic.AtomicBoolean;

import com.revrobotics.CANError;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.jni.CANSparkMaxJNI;

import edu.wpi.first.wpilibj.SpeedController;

import com.revrobotics.CANPIDController.ArbFFUnits;
import com.revrobotics.CANSparkMax.IdleMode;

public class CANSparkMax implements SpeedController {
    Victor speedcontroller;
    CANEncoder canEncoder;
    CANPIDController canPIDController;
    double currentPosition = 0;
    double Position = 0;
    double driveSpeed = 0;
    boolean Inverted = false;
    int id;
    String motorName = "";
    public CANSparkMax(int deviceID, MotorType type) {
        switch(deviceID){
            case 12: 
                id = 12;
                motorName = "FrontLeft";
                break;
            case 5:
                id = 13;
                motorName = "FrontRight";
                break;
            case 3:
                id = 14;
                motorName = "BackLeft";
                break;
            case 4:
                id = 15;
                motorName = "BackRight";
                break;
            case 13:
                id = 16;
                motorName = "shooter";
                break;
        }
        speedcontroller = new Victor(id);
    }
    @Override
    public void set(double speed) {
        driveSpeed = speed;
//        System.out.println(motorName+" set:"+driveSpeed);
    }
    @Override
    public double get(){
//        System.out.println(motorName+" get:"+driveSpeed);
        return driveSpeed;
    }

    @Override
    public void setInverted(boolean inverted){
        Inverted = inverted;
    }

    @Override
    public boolean getInverted(){
        return Inverted;
    }

    public void close() {
        speedcontroller.close();
        return;
    }

    public CANPIDController getPIDController() {
        if (canPIDController == null) {
            canPIDController = new CANPIDController(this);
        }
        return canPIDController;
    }

    public CANEncoder getEncoder() {
        if (canEncoder == null) {
            canEncoder = new CANEncoder(this);
        }
        return canEncoder;
    }

    public void pidWrite(double output) {
    }

    @Override
    public void disable() {
    }

    @Override
    public void stopMotor() {

    }

	public void restoreFactoryDefaults() {
	}

	public void setIdleMode(IdleMode kcoast) {
	}

}
