package frc.robot;

import java.lang.reflect.Array;

import edu.wpi.first.wpilibj.Joystick;

public class DaBearsJoystick  {
    Joystick joystick;
    public static int buttonI = 0;
    public static boolean[] buttonResponse = {true,false,true,true,true,false,true,false,true,false,false,false,false,true,true,false,true,false,true,false,false,false,true,true,false,true,false,true,true,true,false,false,false,true,true,true,true,true,true,true};

    public static int joystickI = 0;
    public static double[] joystickResponse = {0,1,-1,0,.5,-.5,0,0,-.3,.3,.7,.6,-.6,-.9,0,0,0,1,1,-1,-1,.5,.5,.1,.1,0,0,.01,.09,.08,.06,.05,.4,.3,.9,0,0,0,0,0,0,0,0,0,0,0};

    public DaBearsJoystick(int pwd){
        joystick = new Joystick(pwd);
    }

    public double getRawAxis(int axis){
        System.out.println("junit.RawAxis:"+joystickResponse[joystickI]);
        return joystickResponse[joystickI++];
    };

    public boolean getRawButton(int button){
        System.out.println("junit.RawButton:"+buttonResponse[buttonI]);
        return buttonResponse[buttonI++];
    };

    public int getPOV(){
        System.out.println("junit.RawButton:"+buttonResponse[buttonI]);
        return (int)joystickResponse[joystickI++];
    }

    // for debugging only junit tests
    /**/public void resetIndexes(){
    /**/    buttonI = 0;
    /**/    joystickI = 0;
    /**/}
    /**/public void setButtonResp(boolean resp1,boolean resp2, boolean resp3){
    /**/    resetIndexes();
    /**/    boolean b[] = {true,true,true};
    /**/    b[0] = resp1;
    /**/    b[1] = resp2;
    /**/    b[2] = resp3;
    /**/    buttonResponse = b;
    /**/}
    /**/public void setButtonResp(boolean[] resp){
    /**/    resetIndexes();
    /**/    buttonResponse = resp;
    /**/}
    /**/public void setJoystickResp(double[] resp){
    /**/    resetIndexes();
    /**/    joystickResponse = resp;
    /**/}
    // for debugging only junit tests
}