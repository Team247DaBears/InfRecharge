package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;


public class Lifter
{
    DoubleSolenoid.Value lastSet;
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
    lastSet =DoubleSolenoid.Value.kForward;
    Devices.lowLifter.set(DoubleSolenoid.Value.kForward);
    Devices.highLifter.set(DoubleSolenoid.Value.kForward);
    break;
   
    case Down:
    lastSet =DoubleSolenoid.Value.kReverse;
    Devices.lowLifter.set(DoubleSolenoid.Value.kReverse);
    Devices.highLifter.set(DoubleSolenoid.Value.kReverse);
    break;

    case Hoist:
    DoubleSolenoid.Value UpLow = Devices.lowLifter.get();
    DoubleSolenoid.Value UpHigh = Devices.highLifter.get();
    // TODO determine if this test should be kOff and track that the last was forward
    if ((UpLow==DoubleSolenoid.Value.kForward && UpHigh==DoubleSolenoid.Value.kForward) ||
        (UpLow==DoubleSolenoid.Value.kOff && UpHigh==DoubleSolenoid.Value.kOff && lastSet==DoubleSolenoid.Value.kForward)){
        Devices.lifterHoist.set(1);
    }
    break;

    case Hold:
    break;

}

}
}



