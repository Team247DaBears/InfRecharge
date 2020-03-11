package frc.robot;

import java.lang.reflect.Array;

//import edu.wpi.first.wpilibj.Joystick;


public class Joystick extends  edu.wpi.first.wpilibj.GenericHID {
    public static int buttonI = 0;
    public static boolean[] buttonResponse = {true,false,true,true,true,false,true,false,true,false,false,false,false,true,true,false,true,false,true,false,false,false,true,true,false,true,false,true,true,true,false,false,false,true,true,true,true,true,true,true};

    public static int joystickI = 0;
    public static double[] joystickResponse = {0,1,-1,0,.5,-.5,0,0,-.3,.3,.7,.6,-.6,-.9,0,0,0,1,1,-1,-1,.5,.5,.1,.1,0,0,.01,.09,.08,.06,.05,.4,.3,.9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public static final boolean[] initbutton = {true,false,true,true,true,false,true,false,true,false,false,false,false,true,true,false,true,false,true,false,false,false,true,true,false,true,false,true,true,true,false,false,false,true,true,true,true,true,true,true};
    public static final double[] initjoystick = {0,1,-1,0,.5,-.5,0,0,-.3,.3,.7,.6,-.6,-.9,0,0,0,1,1,-1,-1,.5,.5,.1,.1,0,0,.01,.09,.08,.06,.05,.4,.3,.9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public int port;

    public Joystick(int port){
        super(port);
        System.out.println("init Joystick");
        buttonResponse = initbutton;
        joystickResponse = initjoystick;
        buttonI = 0;
        joystickI = 0;
    }

    public void setOutputs(int value) {
        buttonI = 0;
        joystickI = 0;
        return;
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
    public void setOutput(int i, boolean value) {
        buttonI = 0;
        joystickI = 0;
        buttonResponse[i] = value;
        return;
    }
    public void wait(double value, int i) {
        // TODO Auto-generated method stub
        buttonI = 0;
        joystickI = 0;
        joystickResponse[i] = value;
        return;
    }
 @Override
 public double getY(Hand hand) {
     return 0;
 }
@Override
public double getX(Hand hand) {
    return 0;
}
}
