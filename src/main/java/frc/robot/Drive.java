package frc.robot;
import frc.robot.UserInput;
import frc.robot.Devices;
public class  Drive
{

  public GearStates currentGearState;

    public Drive() {
        currentGearState=GearStates.LowGearOff;
    }
    public void drive()
      {

          double leftSideMotorSpeed= UserInput.getLeftStick();
          double rightSideMotorSpeed=UserInput.getRightStick();
System.out.println("Ls  "+leftSideMotorSpeed);
System.out.println("Rs  "+rightSideMotorSpeed);
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
              break;
          case LowGearOff:
          case LowGearPressed:
              Devices.gearShift.set(false);
              break;
      }
  }
//  System.out.println("Gear state after is "+currentGearState.toString());

}       