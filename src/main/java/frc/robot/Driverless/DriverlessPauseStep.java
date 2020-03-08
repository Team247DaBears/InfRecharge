/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Driverless;

/**
 * Add your docs here.
 */
public class DriverlessPauseStep extends DriverlessStep {

    long timeMs;

    public DriverlessPauseStep(DriverlessProgram _parent,  long _timeMs)
    {
        parent=_parent;
        timeMs=_timeMs;
    }


    @Override
    public void start()
    {
        super.start();
    }

    @Override
    public void execute()
    {
        //Do nothing to pause
    }

    @Override
    public void abort()
    {
        parent.abort();
    }

    @Override
    public void stop()
    {
        //Do nothing to stop
    }

    @Override
    public boolean isComplete()
    {
        if (System.currentTimeMillis()-startTime>timeMs)
            return true;
        else 
            return false;
    }
}
