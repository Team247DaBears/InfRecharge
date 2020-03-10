package frc.robot;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANPIDController.ArbFFUnits;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class CANPIDController extends com.revrobotics.CANPIDController {

    CANSparkMax speedController;
    CANEncoder encoder;
    
    public CANPIDController(CANSparkMax device) {
        super(device);
        // TODO Auto-generated constructor stub
        speedController = device;
    }

    public CANError setP(double kp) {
        return CANError.kOk;
    }
    public CANError setD(double kd) {
        return CANError.kOk;
    }
    public CANError setI(double ki) {
        return CANError.kOk;
    }
    public CANError setOutputRange(double mino, double maxo) {
            speedController.set(speedController.get()+.001); // set the speed very slow for testing
            return CANError.kOk;
        }
    public CANError setIZone(double izone) {
        return CANError.kOk;
    }
    public CANError setFF(double ff) {
        return CANError.kOk;
    }
}
