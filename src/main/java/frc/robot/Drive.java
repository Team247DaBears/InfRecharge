package frc.robot;
import frc.robot.UserInput;
import frc.robot.Devices;

public class  Drive
{

  public GearStates currentGearState;


    public void Init() {

    }

    
    public Drive() {
        currentGearState=GearStates.LowGear;
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

      
   

      public void shiftGears()
      {
          if (UserInput.getLowGear())
          {
              currentGearState=GearStates.LowGear;
              Devices.gearShift.set(true);
          }
          else if (UserInput.getHighGear())
          {
              currentGearState=GearStates.HighGear;
              Devices.gearShift.set(false);
          }
      
  }

}       