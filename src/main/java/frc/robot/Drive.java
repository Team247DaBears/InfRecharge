package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.DriverStation;
import frc.robot.Devices;
public class  Drive
{

  private GearStates currentGearState=GearStates.LowGearOff;

    
    public void drive()
      {
          

          double leftSideMotorSpeed= DriverStation.getLeftSpeed();
          double rightSideMotorSpeed=DriverStation.getRightSpeed();

         Devices.backLeft.set(leftSideMotorSpeed);
         Devices.frontLeft.set(leftSideMotorSpeed);
         Devices.frontRight.set(rightSideMotorSpeed);
          Devices.backRight.set(rightSideMotorSpeed);
             shiftGears();// This could go in robot.java
      
      }
   
    
    public void shiftGears(){

    if (DriverStation.getGearButton()) System.out.println("true");
    else System.out.println("False");
    //System.out.println("Gear button is "+UserCom.getGearButton());
//System.out.println("Gear state before is "+currentGearState.toString());
    switch(currentGearState)
    {
        case HighGearOff:
            if(DriverStation.getGearButton())
            {
                currentGearState=GearStates.LowGearPressed;
            }
            break;
       case HighGearPressed:
            if (!DriverStation.getGearButton())
            {
                currentGearState=GearStates.HighGearOff;
            }
            break;
      case LowGearOff:
           if (DriverStation.getGearButton())
           {
               currentGearState=GearStates.HighGearPressed;
           }
           break;
      case LowGearPressed:
           if (!DriverStation.getGearButton())
           {
               currentGearState=GearStates.HighGearOff;
           }
           break;
          
      }
      switch(currentGearState)
      {
          case HighGearOff:
          case HighGearPressed:
          Devices.gearShift.set(DoubleSolenoid.Value.kForward);
              break;
          case LowGearOff:
          case LowGearPressed:
              Devices.gearShift.set(DoubleSolenoid.Value.kReverse);
              break;
      }
  }
//  System.out.println("Gear state after is "+currentGearState.toString());

}       