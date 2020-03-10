package frc.robot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import com.revrobotics.CANPIDController.ArbFFUnits;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class CANSparkMax extends  com.revrobotics.CANSparkMax {
    CANEncoder canEncoder;
    CANPIDController canPIDController;
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
    public void close() {
        return;
    }

    public CANSparkMax(int deviceID, MotorType type) {
        super(deviceID, type);
        // TODO Auto-generated constructor stub
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

}
