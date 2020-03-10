package frc.robot;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANPIDController.ArbFFUnits;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class CANEncoder extends com.revrobotics.CANEncoder {

    CANSparkMax speedController;
    CANPIDController canPIDController;
    double currentPosition;
    public CANEncoder(CANSparkMax device) {
        super(device);
        // TODO Auto-generated constructor stub
        speedController = device;
    }

    public double getPosition() {
        updatePosition();
        return currentPosition;
    }

    public void updatePosition() {
        double speed = speedController.get();
        double sign = java.lang.Math.signum(speed);
        currentPosition = currentPosition + (speed * sign);
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

}
