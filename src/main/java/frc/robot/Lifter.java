/*----------------------------------------------------------------------------*/

/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */

/* Open Source Software - may be modified and shared by FRC teams. The code   */

/* must be accompanied by the FIRST BSD license file in the root directory of */

/* the project.                                                               */

/*----------------------------------------------------------------------------*/



package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Lifter {
    public LifterStates lifterState = LifterStates.LifterDown;
    //private SpeedController left_winch;    junit use devices version
    //private SpeedController right_winch;   junit use devices version
    //private DoubleSolenoid solenoid;       junit use devices version
    private final double WINCH_SPEED=0.70; 

    public void Init() {
        //left_winch=Devices.lifter_left_motor;  //This isn't very good, but I'm in a hurry
        //right_winch=Devices.lifter_right_motor;
        //solenoid=Devices.lifter_solenoid;
    }


    public void operate()
    {

        boolean up = UserInput.getLifterUp();
        boolean down = UserInput.getLifterDown();
        boolean climb = UserInput.getLifterClimb();

        if (up)/* added to protect against user error */
        {
            Devices.lifter_solenoid.set(DoubleSolenoid.Value.kForward);
            lifterState = LifterStates.LifterUp; // state captured for recording of manual actions to automate
        }
        else if (down)
        {
            Devices.lifter_solenoid.set(DoubleSolenoid.Value.kReverse);
            lifterState = LifterStates.LifterDown;   // state captured for recording of manual actions to automate
        }


        if (climb)
        {
            Devices.lifter_left_motor.set(WINCH_SPEED);
            Devices.lifter_right_motor.set(WINCH_SPEED);
        }
        else
        {
            Devices.lifter_left_motor.set(0);
            Devices.lifter_right_motor.set(0);
        }
        

    }

    public void operate_Production(){

        boolean up = UserInput.getLifterUp();
        boolean down = UserInput.getLifterDown();
        boolean climb = UserInput.getLifterClimb();
        if (up)/* added to protect against user error */
        {
            Devices.lifter_solenoid.set(DoubleSolenoid.Value.kForward);
            lifterState = LifterStates.LifterUp; // state captured for recording of manual actions to automate
        }
        else if (down)
        {
            Devices.lifter_solenoid.set(DoubleSolenoid.Value.kReverse);
            lifterState = LifterStates.LifterDown;   // state captured for recording of manual actions to automate
        }
        else if (climb && 
            (Devices.lifter_solenoid.get() == DoubleSolenoid.Value.kOff || /* protect against */
            Devices.lifter_solenoid.get() == DoubleSolenoid.Value.kForward || /* protect against */
            lifterState == LifterStates.LifterUp ||         /* running up hoist */
            lifterState == LifterStates.LifterHoldUp ||         /* running up hoist */
            lifterState == LifterStates.LifterHoist))           /* when lifter down */
        {
            Devices.lifter_left_motor.set(WINCH_SPEED);
            Devices.lifter_right_motor.set(WINCH_SPEED);
            lifterState = LifterStates.LifterHoist; // state captured for recording of manual actions to automate
        }
        else
        {
            // state captured for recording of manual actions to automate
            /**/if (lifterState == LifterStates.LifterHoist ||
            /**/    lifterState == LifterStates.LifterUp) {
            /**/    lifterState = LifterStates.LifterHoldUp;
            /**/}
            /**/else if (Devices.lifter_solenoid.get() == DoubleSolenoid.Value.kForward) {
            /**/    lifterState = LifterStates.LifterUp;
            /**/}
            /**/else if (Devices.lifter_solenoid.get() == DoubleSolenoid.Value.kReverse) {
            /**/    lifterState = LifterStates.LifterDown;
            /**/}
            /**/else if (Devices.lifter_solenoid.get() == DoubleSolenoid.Value.kOff ||
            /**/        lifterState == LifterStates.LifterUp) {
            /**/    lifterState = LifterStates.LifterHoldUp;
            /**/}
            /**/else {
            /**/    lifterState = LifterStates.LifterHoldDown;    
            /**/}
            // state captured for recordingHol of manual actions to automate
        }
    }
}