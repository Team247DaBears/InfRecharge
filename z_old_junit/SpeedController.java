package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANPIDController.ArbFFUnits;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


import java.lang.Math.*;

public class SpeedController extends edu.wpi.first.wpilibj.SpeedController {
    double currentPosition = 0;
    double Position = 0;
    double driveSpeed = 0;
    boolean Inverted = false;

    public void set(double speed) {
        driveSpeed = speed;
    }
    public double get(){
        return driveSpeed;
    }
    public void setInverted(boolean inverted){
        Inverted = inverted;
    }
    public boolean getInverted(){
        return Inverted;
    }
    public boolean close() {
        return true;
    }
}