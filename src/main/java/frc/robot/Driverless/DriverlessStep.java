/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Driverless;

/**
 * The base class for Driverless Step operations.
 * It is impossible to create one of these, due to the abstract keyword
 */
public abstract class DriverlessStep {

    public long startTime;
    public DriverlessProgram parent;

    public void start()
    {
        startTime=System.currentTimeMillis();
       
    }

    public void execute()
    {

    }

    public void abort()
    {

    }

    public boolean isComplete()
    {
        return true;
    }

    public void stop()
    {
        
    }
}
