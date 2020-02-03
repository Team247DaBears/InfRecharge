package frc.robot;
import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.SpeedController;

public class testableSparkMax  {

    public static SpeedController Init(int pwm, MotorType type, boolean SparkMax) {
        return new Victor(pwm);
    }
}