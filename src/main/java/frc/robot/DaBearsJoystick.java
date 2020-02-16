package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class DaBearsJoystick {
    Joystick joystick;

    public DaBearsJoystick(int pwd){
        joystick = new Joystick(pwd);
    }

    public double getRawAxis(int axis){
        return joystick.getRawAxis(axis);
    };

    public boolean getRawButton(int button){
        return joystick.getRawButton(button);
    };

    // for debugging only junit tests
    /**/public void resetIndexes(){
    /**/}
    // for debugging only junit tests
}