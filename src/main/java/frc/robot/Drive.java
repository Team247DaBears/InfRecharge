package frc.robot;
import frc.robot.UserInput;
import frc.robot.Devices;
import com.revrobotics.ControlType;
public class  Drive
{

  public GearStates currentGearState;
    //Parameteres for velocity control PID on SparkMax
    private final double KP=5e-5;
    private final double KI=2e-6;
    private final double KD=0;
    private final double MAXOUT=1;
    private final double MINOUT=-1;
    private final double FFVALUE=-0.22;  //Will require experimentation to set a better value
    private final double IZONE=200;
    private final double TARGETRPM=-1000;  //Will begin with a single setpoint.  We'll modify that for multiple distance ranges later.

    public void Init() {
        
        InitEncoderController(Devices.frontLeft);
        InitEncoderController(Devices.frontRight);
        InitEncoderController(Devices.backLeft);
        InitEncoderController(Devices.backRight);
    }
    public void InitEncoderController(DaBearsSpeedController motor) {
        motor.set(0);
        motor.setP(KP);
        motor.setD(KD);
        motor.setI(KI);
        motor.setOutputRange(MINOUT, MAXOUT);
        motor.setIZone(IZONE);
        motor.setFF(FFVALUE/TARGETRPM);
        motor.setReference(0, ControlType.kPosition);
    }
    public Drive() {
        currentGearState=GearStates.LowGearOff;
    }
    public void drive()
      {

          double leftSideMotorSpeed= UserInput.getLeftStick();
          double rightSideMotorSpeed=UserInput.getRightStick();
//System.out.println("Ls  "+leftSideMotorSpeed);
//System.out.println("Rs  "+rightSideMotorSpeed);
         Devices.backLeft.set(leftSideMotorSpeed);
         Devices.frontLeft.set(leftSideMotorSpeed);
         Devices.frontRight.set(rightSideMotorSpeed);
          Devices.backRight.set(rightSideMotorSpeed);
             shiftGears();// This could go in robot.java
             
      }

      public void motorTest()
      {
             Devices.frontLeft.set(0);
             Devices.frontRight.set(0);
             Devices.backLeft.set(0);
             Devices.backRight.set(0);

             double speed=-1*UserInput.rightStick.getRawAxis(1);
             if (UserInput.rightStick.getRawButton(1))
                 Devices.frontLeft.set(speed);
             if (UserInput.rightStick.getRawButton(2))
                Devices.frontRight.set(speed);
             if (UserInput.rightStick.getRawButton(3))
                Devices.backLeft.set(speed);
             if (UserInput.rightStick.getRawButton(4))
                Devices.backRight.set(speed);
      
      }
   
    public void shiftGears(){

   // if (UserInput.getGearButton()) System.out.println("true");
  //  else System.out.println("False");
    //System.out.println("Gear button is "+UserCom.getGearButton());
//System.out.println("Gear state before is "+currentGearState.toString());
    switch(currentGearState)
    {
        case HighGearOff:
            if(UserInput.getGearButton())
            {
                currentGearState=GearStates.LowGearPressed;
            }
            break;
       case HighGearPressed:
            if (!UserInput.getGearButton())
            {
                currentGearState=GearStates.HighGearOff;
            }
            break;
      case LowGearOff:
           if (UserInput.getGearButton())
           {
               currentGearState=GearStates.HighGearPressed;
           }
           break;
      case LowGearPressed:
           if (!UserInput.getGearButton())
           {
               currentGearState=GearStates.HighGearOff;
           }
           break;
          
      }
      switch(currentGearState)
      {
        case HighGearOff:
        case HighGearPressed:
                Devices.gearShift.set(true);
                Devices.setMotorConversionHigh();
                break;
        case LowGearOff:
        case LowGearPressed:
            Devices.gearShift.set(false);
              Devices.setMotorConversionLow();
              break;
      }
  }
//  System.out.println("Gear state after is "+currentGearState.toString());

}       