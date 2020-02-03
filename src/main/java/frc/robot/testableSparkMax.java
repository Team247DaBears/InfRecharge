package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder.*;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class testableSparkMax {
    public static SpeedController Init(int pwm, MotorType type,boolean SparkMax) {
        if (SparkMax) {
            CANSparkMax motor = new CANSparkMax(pwm,type);
            SpeedController speedcontroller = motor;
            return speedcontroller;
        }
        else {
            return new Victor(pwm);
        }
    }
    public static Encoder Init(SpeedController motor, boolean SparkMax) {
        if (SparkMax) {
            Object encoder = new CANEncoder((CANSparkMax)motor);
            return (Encoder) encoder;
        }
        else {
            return new Encoder(0, 0);
        }
    }
}