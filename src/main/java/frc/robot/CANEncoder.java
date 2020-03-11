package frc.robot;

import java.util.concurrent.atomic.AtomicBoolean;
import edu.wpi.first.wpilibj.SpeedController;


import com.revrobotics.CANError;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ControlType;

public class CANEncoder extends CANSensor {
    CANSparkMax speedController = null;
    CANPIDController canPIDController;
    double currentPosition=0;
    double targetPosition=0;
    boolean Inverted=false;
    ControlType controlType;
 
    public CANEncoder(CANSparkMax device) {
        super(device);
        speedController = device;
    }

    public double getPosition() {
        updatePosition();
//        System.out.println("current Pos:"+currentPosition);
        return currentPosition;
    }
    public boolean getInverted() {
        return Inverted;
    }
    public void getInverted(boolean inverted) {
        Inverted=inverted;
    }
    public void updatePosition() {
        double speed = speedController.get();
        double sign = java.lang.Math.signum(targetPosition);
        currentPosition = currentPosition + (speed * sign);
        if (speed == 0) {
            speedController.set(.001); // set the speed very slow for testing
        }
    if (sign==-1) {
            if (currentPosition<targetPosition) { 
//                System.out.println("reset<:"+speed+","+sign+","+currentPosition+"."+targetPosition);
                currentPosition = targetPosition;
            }
        }
        else {
            if (currentPosition>targetPosition) { 
//                System.out.println("reset>:"+speed+","+sign+","+currentPosition+"."+targetPosition);
                currentPosition = targetPosition;
            }
        }
        speedController.set(speed+speed+speed+speed+speed+speed+speed+speed);
    }

    public double getVelocity() {
        return speedController.get(); // otherwise return speed
    }
    public void restoreFactoryDefaults() {
        return;
    }
    public void setIdleMode(IdleMode mode) {
        return;
    }
    public CANError setPositionConversionFactor(double confFactor) {
        return CANError.kOk; 
    }
    public CANError setReference(double position, ControlType ct) {
        switch (ct) {
            case kPosition:
                if (speedController.get() > 7) {
                    speedController.set(.001); // set the speed very slow for testing
                } 
                break;
            case kVoltage:
            case kVelocity:
            case kDutyCycle:
                if (speedController.get() > 7){
                    speedController.set(position); // set the speed very slow for testing
                }
            break;
        }
        targetPosition = position;
        controlType = ct;
        return CANError.kOk; 
    }

    public CANError setPosition(double position) {
        currentPosition = position;
        controlType = null;
//        speedController.set(0);
        return CANError.kOk; 
    }
    @Override
    protected int getID() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public CANError setInverted(boolean inverted) {
        // TODO Auto-generated method stub
        Inverted = inverted;
        return CANError.kOk; 
    }

}
