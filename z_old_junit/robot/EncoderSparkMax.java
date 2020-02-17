package frc.robot;

import com.revrobotics.*;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class EncoderSparkMax {
    boolean SparkMax;
    Object encoder = null;
    CANEncoder encoderSparkMax = null;
    Encoder encoderVictor = null;

    public EncoderSparkMax(SpeedController motor, boolean sparkMax) {
        SparkMax = sparkMax;
            encoder = new CANEncoder((CANSparkMax)motor);
            encoderVictor = new Encoder(0, 0);
    }
    public double getPosition() {
            return encoderVictor.get();
    }
    public void setPosition(double position) {
            // victor doesn't have setPosition
    }
}