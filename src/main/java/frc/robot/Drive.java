package frc.robot;
import frc.robot.UserInput;
import frc.robot.Devices;

public class  Drive
{




    public void Init() {

    }

    
    public Drive() {
      setGear(true);
    }
    public void drive()
      {

          double leftSideMotorSpeed= UserInput.getLeftStick();
          double rightSideMotorSpeed=UserInput.getRightStick();
//System.out.println("Ls  "+leftSideMotorSpeed);
//System.out.println("Rs  "+rightSideMotorSpeed);
          drive_Command(leftSideMotorSpeed, rightSideMotorSpeed);

             shiftGears();// This could go in robot.java
             
      }


      
    public void drive_Command(double leftSideMotorSpeed, double rightSideMotorSpeed)
    {
      Devices.backLeft.set(leftSideMotorSpeed);
      Devices.frontLeft.set(leftSideMotorSpeed);
      Devices.frontRight.set(rightSideMotorSpeed);
       Devices.backRight.set(rightSideMotorSpeed);
    }

    public void drive_SpeedAndRotation(double forwardSpeed, double rotationRate)
    {
      double l=forwardSpeed+rotationRate;
      double r=forwardSpeed-rotationRate;


      //if either value is greater than 1, normalize to 1
      double mag=Math.max(Math.abs(l),Math.abs(r));
      if (mag>1)
      {
        l=l/mag;
        r=r/mag;
      }

      drive_Command(l, r);
    }

    public void shiftGears()
      {
          if (UserInput.getLowGear())
          {

              setGear(true);
          }
          else if (UserInput.getHighGear())
          {
            setGear(false);
          }
      
  }



      public void setGear(boolean isLowGear)
      {
        Devices.gearShift.set(isLowGear);
      }

}       