package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class DaBearsJoystick  {
    Joystick joystick;
    public static int buttonI = 0;
    private static boolean[] buttonResponse = {true,false,true,true,false,false,true,true,true,false,false,false,false,true,true,true,true,true,true,false,false,false,true,true,false,true,false,true,true,true,false,false,false};

    public static int joystickI = 0;
    private static double[] joystickResponse = {0,1,-1,0,.5,-.5,0,0,-.3,.3,.7,.6,-.6,-.9,0,0,0,1,1,-1,-1,.5,.5,.1,.1,0,0};

    public void resetIndexes(){
        buttonI = 0;
        joystickI = 0;
    }

    public DaBearsJoystick(int pwd){
        joystick = new Joystick(pwd);
    }

    public double getRawAxis(int axis){
        System.out.println("getRawAxis:"+joystickResponse[joystickI]);
        return joystickResponse[joystickI++];
    };

    public boolean getRawButton(int button){
        System.out.println("getRawButton:"+buttonResponse[buttonI]);
        return buttonResponse[buttonI++];
    };
}
