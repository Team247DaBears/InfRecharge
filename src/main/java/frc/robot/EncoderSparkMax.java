package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder.*;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class EncoderSparkMax {
    boolean SparkMax;
    Object encoder = null;
    CANEncoder encoderSparkMax = null;
    Encoder encoderVictor = null;

    public EncoderSparkMax(SpeedController motor, boolean sparkMax) {
        SparkMax = sparkMax;
        if (SparkMax) {
            encoder = new CANEncoder((CANSparkMax)motor);
            encoderSparkMax = (CANEncoder)encoder;
        }
        else {
            encoder = new CANEncoder((CANSparkMax)motor);
            encoderVictor = new Encoder(0, 0);
        }
    }
    public double getPosition() {
        if (SparkMax) {
            return encoderSparkMax.getPosition();
        }
        else {
            return encoderVictor.get();
        }
    }
    public void setPosition(double position) {
        if (SparkMax) {
            encoderSparkMax.setPosition(position);
        }
        else {
            // victor doesn't have setPosition
        }
    }
}