package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class DaBearsJoystick  {
    Joystick joystick;
    public static int buttonI = 0;
    private static boolean[] buttonResponse = {true,false,true,true,true,false,true,false,true,false,false,false,false,true,true,false,true,false,true,false,false,false,true,true,false,true,false,true,true,true,false,false,false,true,true,true,true,true,true,true};

    public static int joystickI = 0;
    private static double[] joystickResponse = {0,1,-1,0,.5,-.5,0,0,-.3,.3,.7,.6,-.6,-.9,0,0,0,1,1,-1,-1,.5,.5,.1,.1,0,0,.01,.09,.08,.06,.05,.4,.3,.9,0,0,0,0,0,0,0,0,0,0,0};

    public DaBearsJoystick(int pwd){
        joystick = new Joystick(pwd);
    }

    public double getRawAxis(int axis){
        return joystickResponse[joystickI++];
    };

    public boolean getRawButton(int button){
        return buttonResponse[buttonI++];
    };

    // for debugging only junit tests
    /**/public void resetIndexes(){
    /**/    buttonI = 0;
    /**/    joystickI = 0;
    /**/}
    // for debugging only junit tests
}
