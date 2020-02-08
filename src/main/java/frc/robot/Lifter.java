package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;


public class Lifter
{
    LifterStates heldPosition=LifterStates.Low;
    
    public void setPosition()
    {
        
        LifterStates commandedPosition=UserInput.getCommandedPosition();
        //System.out.println("cOMMANDED position +"+commandedPosition.toString());
        if (commandedPosition==LifterStates.Hold) commandedPosition=heldPosition;
        heldPosition=commandedPosition;


switch(commandedPosition)
{
    case Low:
    Devices.lowLifter.set(DoubleSolenoid.Value.kForward);
   // Devices.highLifter.set(DoubleSolenoid.Value.kForward);
    break;
   
    case High:
    Devices.lowLifter.set(DoubleSolenoid.Value.kReverse);
  //Devices.highLifter.set(DoubleSolenoid.Value.kReverse);
    break;

    case Hold:
    break;

}

}
}



