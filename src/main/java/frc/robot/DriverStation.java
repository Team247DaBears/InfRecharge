package frc.robot;
import edu.wpi.first.wpilibj.Joystick;

public class DriverStation
{
    private static final int LEFTPORT=0;
    private static final int RIGHTPORT=1;
    private static final int OPERATORPORT=2;

    private static final int Y_AXIS=1;
    private static final int X_AXIS=0;

    private static  Joystick leftStick;
    private static  Joystick rightStick;
    private static Joystick operatorStick;  //driver two

    //buttons on driver stick
    private static final int JSB_GEARSHIFT=1;


    //buttons for driver two
    private static final int JSB_POSITION_0=4;
    private static final int JSB_POSITION_1=2;
    private static final int JSB_POSITION_2=1;
    private static final int JSB_POSITION_3=3;




public static void Init()
    {
        leftStick=new Joystick(LEFTPORT);
        rightStick=new Joystick(RIGHTPORT);
        operatorStick=new Joystick(OPERATORPORT);
    }

    public static double  getLeftSpeed()
    {
        return -1*leftStick.getRawAxis(Y_AXIS);
    }

    public static double getRightSpeed()
    {
        return -1*rightStick.getRawAxis(Y_AXIS);
    }


public static LifterStates getCommandedPosition()
{
    if(operatorStick.getRawButton(JSB_POSITION_0)){
        System.out.println("Button 4 pressed"); 
        return LifterStates.Low;

}
     if (operatorStick.getRawButton(JSB_POSITION_1)){
         System.out.println("Button 2 pressed");
        return LifterStates.High;
}
     else {

        return LifterStates.Hold;
     }
}
}
