package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class DaBearsJoystick {
    Joystick joystick;
    private int buttonI = 0;
    private int joystickI = 0;

    public void resetIndexes(){
        this.buttonI = 0;
        this.joystickI = 0;
    }

    public DaBearsJoystick(int pwd){
        joystick = new Joystick(pwd);
    }

    public double getRawAxis(int axis){
        return joystick.getRawAxis(axis);
    };

    public boolean getRawButton(int button){
        return joystick.getRawButton(button);
    };

}