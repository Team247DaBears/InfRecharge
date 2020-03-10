package frc.robot;

import java.lang.reflect.Array;


public class Joystick extends  edu.wpi.first.wpilibj.Joystick {
    Joystick joystick;
    public static int buttonI = 0;
    public static boolean[] buttonResponse = {true,false,true,true,true,false,true,false,true,false,false,false,false,true,true,false,true,false,true,false,false,false,true,true,false,true,false,true,true,true,false,false,false,true,true,true,true,true,true,true};

    public static int joystickI = 0;
    public static double[] joystickResponse = {0,1,-1,0,.5,-.5,0,0,-.3,.3,.7,.6,-.6,-.9,0,0,0,1,1,-1,-1,.5,.5,.1,.1,0,0,.01,.09,.08,.06,.05,.4,.3,.9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public static final boolean[] initbutton = {true,false,true,true,true,false,true,false,true,false,false,false,false,true,true,false,true,false,true,false,false,false,true,true,false,true,false,true,true,true,false,false,false,true,true,true,true,true,true,true};
    public static final double[] initjoystick = {0,1,-1,0,.5,-.5,0,0,-.3,.3,.7,.6,-.6,-.9,0,0,0,1,1,-1,-1,.5,.5,.1,.1,0,0,.01,.09,.08,.06,.05,.4,.3,.9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    public Joystick(int port){
        super(port);
        joystick = new Joystick(port);
        System.out.println("init Joystick");
        buttonResponse = initbutton;
        joystickResponse = initjoystick;
        buttonI = 0;
        joystickI = 0;
    }

    public double getRawAxis(int axis){
        //System.out.println("junit.RawAxis:"+joystickResponse[joystickI]);
        return joystickResponse[joystickI++];
    };

    public boolean getRawButton(int button){
        //System.out.println("junit.Buttons:"+buttonResponse);
        //System.out.println("junit.ButtonI:"+buttonI);
        //System.out.println("junit.RawButton:"+buttonResponse[buttonI]);
        return buttonResponse[buttonI++];
    };

    public int getPOV(){
        //System.out.println("junit.Buttons:"+buttonResponse);
        //System.out.println("junit.ButtonI:"+buttonI);
        //System.out.println("junit.POV:"+buttonResponse[buttonI]);
        return (int)joystickResponse[joystickI++];
    }

    // for debugging only junit tests
    /**/public void resetIndexes(){
    /**/    buttonI = 0;
    /**/    joystickI = 0;
    /**/}
    /**/public void setButtonResp(boolean resp1,boolean resp2, boolean resp3){
    /**/    System.out.println("setButtonResp");
    /**/    resetIndexes();
    /**/    boolean b[] = {true,true,true};
    /**/    b[0] = resp1;
    /**/    b[1] = resp2;
    /**/    b[2] = resp3;
    /**/    buttonResponse = b;
    /**/}
    /**/public void setButtonResp(boolean[] resp){
    /**/    System.out.println("setButtonResp");
    /**/    resetIndexes();
    /**/    buttonResponse = resp;
    /**/}
    /**/public void setJoystickResp(double[] resp){
    /**/    System.out.println("setJoystickResp");
    /**/    resetIndexes();
    /**/    joystickResponse = resp;
    /**/}
    // for debugging only junit tests
}
