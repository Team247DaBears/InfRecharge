/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Driverless;

import frc.robot.Shooter;


/**
 * Add your docs here.
 */
public class DriverlessShoot extends DriverlessStep {

    private Shooter shooter;
    private long timeLimit;

    public DriverlessShoot( DriverlessProgram _parent, Shooter _shooter, long _timeLimit)
    {
        parent=_parent;
        shooter=_shooter;
        timeLimit=_timeLimit;

    }

    @Override
    public void start()
    {
        super.start();
        shooter.commenceShooting();
    }

    @Override
    public void execute()
    {
        shooter.execute();
    }

    @Override
    public boolean isComplete()
    {
        if (shooter.isAutoShootComplete())
            return true;
        else if (System.currentTimeMillis()-startTime>timeLimit)
        {
            abort();
            return true;
        }
        else return false;
    }

    @Override
    public void stop()
    {
        shooter.resetShooting();
    }
}
