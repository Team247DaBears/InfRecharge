package frc.robot;
import java.lang.Math;

public class UserInput
{
    private static final int LEFTPORT=0;
    private static final int RIGHTPORT=1;
    private static final int OPERATORPORT=2;
    private static final double Deadband=.2;
    
    

    public static  DaBearsJoystick leftStick = null;
    private static  DaBearsJoystick rightStick = null;
    private static  DaBearsJoystick operatorStick = null;  //driver two
  
    

    //buttons and controls on driver stick.  (Left and right are same, for now)

    private static final int X_AXIS=0;
    private static final int Y_AXIS=1;
    //buttons and controls on operator stick (XBox controller)
    private static final int JSB_GEARSHIFT=1;
  
    private static final int JSB_INTAKEDOWN=3;
    private static final int JSB_INTAKEUP=2;
    private static final int JSB_INTAKERUN=1;
  
    private static final int JSB_LIFTERUP=5;
    private static final int JSB_LIFTERDOWN=6;

    private static final int AXIS_HOIST = 2;
    private static final int AXIS_SHOOTER = 5;
    
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
        return -1*getDeadband(leftStick.getRawAxis(Y_AXIS));

    }

    public static double  getRightStick(){
        return -1*getDeadband(rightStick.getRawAxis(Y_AXIS));
       
    }

    public static double  getDeadband(double Joystick_val){ 
                if (Math.abs(Joystick_val)<Deadband) return 0;

                return Math.signum(Joystick_val)*(Math.abs(Joystick_val)-Deadband)/(1-Deadband);
}  


//Lifter up and lifter down are for extending the lifter via the solenoids
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
   //return operatorStick.getRawAxis(AXIS_HOIST)>0.5;
   return false;
}

public static boolean getGearButton()
    {
         
		return rightStick.getRawButton(JSB_GEARSHIFT);
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

    //Activate the sequence to spin up the motor and to increase conveyor speed
    public static boolean getShooting()
    {
        return operatorStick.getRawAxis(AXIS_SHOOTER)>.5;
    }

    //Activate solenoid to tilt the shooter    
    public static boolean getShooterLowShot()
    {
        return operatorStick.getPOV()>=135 && operatorStick.getPOV()<=225;
    }
}

  
