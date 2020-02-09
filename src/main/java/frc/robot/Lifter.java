package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;


public class Lifter
{
    LifterStates heldPosition=LifterStates.Down;
    
    public void setPosition()
    {
        
        LifterStates commandedPosition=UserInput.getCommandedPosition();
        //System.out.println("cOMMANDED position +"+commandedPosition.toString());
        if (commandedPosition==LifterStates.Hold) commandedPosition=heldPosition;
        heldPosition=commandedPosition;


switch(commandedPosition)
{
    case Up:
    Devices.lowLifter.set(DoubleSolenoid.Value.kForward);
    Devices.highLifter.set(DoubleSolenoid.Value.kForward);
    break;
   
    case Down:
    Devices.lowLifter.set(DoubleSolenoid.Value.kReverse);
    Devices.highLifter.set(DoubleSolenoid.Value.kReverse);
    break;

    case Hoist:
    Devices.lifterHoist.set(1);
    break;

    case Hold:
    break;

}

}
}



