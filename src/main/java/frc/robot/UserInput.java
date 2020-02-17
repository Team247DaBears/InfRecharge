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
    public static  DaBearsJoystick rightStick = null;
    public static  DaBearsJoystick operatorStick = null;  //driver two
  
    private static final int JSB_ROLLERSFORWARD=0;
      private static final int JSB_ROLLERSREVERSE=1;
      private static final int AXIS_CLAWOPENCLOSE=2;
  
    //buttons on driver stick
    private static final int JSB_GEARSHIFT=1;
  
    private static final int JSB_INTAKEDOWN=4;
    private static final int JSB_INTAKEUP=2;
    private static final int JSB_INTAKERUN=1;
  
    private static final int JSB_LIFTERUP=5;
    private static final int JSB_LIFTERDOWN=6;
    private static final int JSB_LIFTERHOIST=7;
    private static final int JSB_WRITERECORDER=8;

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
        return getDeadband(leftStick.getRawAxis(Y_AXIS));
    }

    public static double  getRightStick(){
        return getDeadband(rightStick.getRawAxis(Y_AXIS));
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
    return operatorStick.getRawButton(JSB_LIFTERHOIST);
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
        boolean right = rightStick.getRawButton(JSB_WRITERECORDER);
        boolean left = leftStick.getRawButton(JSB_WRITERECORDER);
        return right && left;
    }
}

  
