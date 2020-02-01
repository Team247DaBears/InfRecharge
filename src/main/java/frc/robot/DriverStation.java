package frc.robot;
import edu.wpi.first.wpilibj.Joystick;
import java.lang.Math;

public class DriverStation
{
    private static final int LEFTPORT=0;
    private static final int RIGHTPORT=1;
    private static final int OPERATORPORT=2;
private static final double Deadband=8D;
    private static final int Y_AXIS=1;
    private static final int X_AXIS=0;

    private static  Joystick leftStick;
    private static  Joystick rightStick;
    private static Joystick operatorStick;  //driver two
  
    private static final int JSB_ROLLERSFORWARD=0;
      private static final int JSB_ROLLERSREVERSE=1;
      private static final int AXIS_CLAWOPENCLOSE=2;
  
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
    
    {double Joystick_calc=0; double JoystickValue=leftStick.getRawAxis(Y_AXIS);double Joystick_val=0;
        if(java.lang.Math.abs(JoystickValue) < Deadband ){
            Joystick_calc = 0;}
            else{
 
			Joystick_calc = (1 / (1 - Deadband)) * (Joystick_val + (-java.lang.Math.signum(Joystick_val) * Deadband));}
           
              return -1*Joystick_calc;
    }

    public static double getRightSpeed()
     {double Joystick_calc=0; double JoystickValue=leftStick.getRawAxis(X_AXIS);double Joystick_val=0;
            if(java.lang.Math.abs(JoystickValue) < Deadband ){
                Joystick_calc = 0;}
                else{
     
                Joystick_calc = (1 / (1 - Deadband)) * (Joystick_val + (-java.lang.Math.signum(Joystick_val) * Deadband));}
               
                  return -1*Joystick_calc;}

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




public static boolean getGearButton()
    {
         
		return rightStick.getRawButton(JSB_GEARSHIFT);
    }

    public static boolean getClawButton()
    {
         if (operatorStick.getRawAxis(AXIS_CLAWOPENCLOSE)>0.5) return true;
        else return false;
    }

    public static boolean rollersForward()
    {
        return operatorStick.getRawButton(JSB_ROLLERSFORWARD);
    }

    public static boolean rollersReverse()
    {
        return operatorStick.getRawButton(JSB_ROLLERSREVERSE);
    }
}

  
