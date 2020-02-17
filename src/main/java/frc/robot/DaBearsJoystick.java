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
    /**/public void resetIndexes(){}
    /**/public void setButtonResp(boolean resp1, boolean resp2, boolean resp3){}
    /**/public void setButtonResp(boolean[] resp){}
    /**/public void setJoystickResp(double[] resp){}
    // for debugging only junit tests
}