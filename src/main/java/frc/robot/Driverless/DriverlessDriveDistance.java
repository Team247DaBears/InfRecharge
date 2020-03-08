/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Driverless;

import com.revrobotics.CANEncoder;

import frc.robot.Devices;
import frc.robot.Drive;

/**
 * Add your docs here.
 */
public class DriverlessDriveDistance extends DriverlessStep {

    public Drive drive;
    public double distance;
    public long timeLimit;



    private double startingPositionLeft;
    private double startingPositionRight;
    private CANEncoder encLeft;
    private CANEncoder encRight;

    private double tolerance=0;

    private final double DRIVERLESS_SPEED=0.5;

    public DriverlessDriveDistance(DriverlessProgram _parent,  Drive _drive, double _distance, long _timeLimit)
    {
        parent=_parent;
        timeLimit=_timeLimit;
        distance=_distance;
        drive=_drive;
        encLeft=Devices.frontLeft.getEncoder();
        encRight=Devices.frontRight.getEncoder();

        tolerance=0.1*distance;//The degree of error that can be tolerated between right and left

    }

    @Override
    public void start()
    {
        super.start();

        startingPositionLeft=encLeft.getPosition();
        startingPositionRight=encRight.getPosition();
    }

    @Override
    public boolean isComplete()
    {
        double leftTravelled=encLeft.getPosition()-startingPositionLeft;
        double rightTravelled=encRight.getPosition()-startingPositionRight;
        

        if ( Math.abs(leftTravelled-rightTravelled)>tolerance)
        {
            abort();
            return true;
        }
        else
        {
            double travelled=(leftTravelled+rightTravelled)/2.0;
            if (distance>0)
            {
                if (travelled>distance) return true;
            }
            else
            {
                if (travelled<distance) return true;
            }
        }


        if  (System.currentTimeMillis()-startTime>timeLimit)
        {
            abort();
            return true;
        }


        //if you get to here, nothing else caused it to return
        return false;
        

    }

    @Override
    public void abort()
    {
        drive.drive_Command(0, 0);
        parent.abort();



    }

    @Override
    public void execute()
    {
        drive.drive_Command(DRIVERLESS_SPEED,DRIVERLESS_SPEED);
    }
}
