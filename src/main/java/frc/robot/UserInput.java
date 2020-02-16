package frc.robot;
import java.lang.Math;

public class UserInput
{
    private static final int LEFTPORT=0;
    private static final int RIGHTPORT=1;
    private static final int OPERATORPORT=2;
    private static final double Deadband=.2;
    private static final int Y_AXIS=1;
    private static final int X_AXIS=0;

    public static  DaBearsJoystick leftStick = null;
    private static  DaBearsJoystick rightStick = null;
    private static  DaBearsJoystick operatorStick = null;  //driver two
  
    private static final int JSB_ROLLERSFORWARD=0;
      private static final int JSB_ROLLERSREVERSE=1;
      private static final int AXIS_CLAWOPENCLOSE=2;
  
    //buttons on driver stick
    private static final int JSB_GEARSHIFT=1;
  
    private static final int JSB_INTAKEDOWN=3;
    private static final int JSB_INTAKEUP=2;
    private static final int JSB_INTAKERUN=1;
  
    private static final int JSB_LIFTERUP=5;
    private static final int JSB_LIFTERDOWN=6;
    private static final int JSB_LIFTERHOIST=7;
    private static final int JSB_WRITERECORDER=8;

    private static final int JSB_FEED=99;//Obviously, it isn't 99
    private static final int JSB_SHOOT=99;

public static void Init()
    {
        if (leftStick==null) {
            leftStick=new DaBearsJoystick(LEFTPORT);
            rightStick=new DaBearsJoystick(RIGHTPORT);
            operatorStick=new DaBearsJoystick(OPERATORPORT);
        }
    }

    public static double  getLeftStick()    
    {
      //  return getDeadband(leftStick.getRawAxis(Y_AXIS));
      return 0;
    }

    public static double  getRightStick(){
       // return getDeadband(rightStick.getRawAxis(Y_AXIS));
        return 0;
    }

    public static double  getDeadband(double Joystick_val){ 
                if (Math.abs(Joystick_val)<Deadband) return 0;

                return Math.signum(Joystick_val)*(Math.abs(Joystick_val)-Deadband)/(1-Deadband);
}  

public static boolean getLifterUp()
{
    return operatorStick.getRawButton(JSB_LIFTERUP);
}

public static boolean getLifterDown()
{
    return operatorStick.getRawButton(JSB_LIFTERDOWN);
}

public static boolean getLifterClimb()
{
   // return operatorStick.getRawButton(JSB_LIFTERHOIST);
   //return operatorStick.getRawAxis(2)>0.5;
   return false;
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
    public static boolean intakeUp()
    {
        return operatorStick.getRawButton(JSB_INTAKEUP);
    }
    public static boolean intakeDown()
    {
        return operatorStick.getRawButton(JSB_INTAKEDOWN);
    }
    public static boolean intakeRun()
    {
        return operatorStick.getRawButton(JSB_INTAKERUN);
    }
    public static boolean writeRecorder()
    {
        return rightStick.getRawButton(JSB_WRITERECORDER) && leftStick.getRawButton(JSB_WRITERECORDER);
    }

    public static boolean getFeeding()
    {
        //return operatorStick.getRawButton(JSB_FEED);
        return operatorStick.getRawAxis(3)>.5;
    }

    public static boolean getShooting()
    {
        //return operatorStick.getRawButton(JSB_SHOOT);

        return operatorStick.getRawAxis(3)>.5;
    }
}

  
