package frc.robot;

import java.util.ResourceBundle.Control;

import com.revrobotics.CANError;
import com.revrobotics.ControlType;


public class CANPIDController {

    CANSparkMax speedController;
    CANEncoder encoder;
    ControlType controlType;
    
    public CANPIDController(CANSparkMax device) {
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
  //          System.out.println("speed Output Range:"+.001);
            speedController.set(.001); // set the speed very slow for testing
            return CANError.kOk;
        }

    public CANError setIZone(double izone) {
        return CANError.kOk;
    }

    public CANError setFF(double ff) {
        return CANError.kOk;
    }

	public void setReference(double position, ControlType ct) {
//        System.out.println("speed Reference:"+.001);
        speedController.getEncoder().setReference(position,ct); // set the speed very slow for testing
        controlType = ct;
    }
}