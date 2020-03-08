/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Driverless;

import java.util.ArrayList;
import java.util.List;




/**
 * Add your docs here.
 */
public class DriverlessProgram {

    public boolean isAborted=false;
    public boolean isRunning=false;
    public String name;

    List<DriverlessStep> steps;

    public int currentStepIndex=0;

    public DriverlessProgram(String _name)
    {
        name=_name;
        steps=new ArrayList<DriverlessStep>();
    }

    public void start()
    {
        if (steps.size()==0)
        {
            abort();
            return;
        }

        //Indicate the program is running, and start the first leg of the program
        currentStepIndex=0;
        isAborted=false;
        isRunning=true;
        steps.get(currentStepIndex).start();
    
    }

    /**
     * This is for an "internal abort" triggered from inside the autonomous loop.
     * It is assumed that nothing else will be running that could send an external abort
     */
    public void abort()
    {
        isAborted=true;
        isRunning=false;
    }

    public void execute()
    {
        if (isRunning && !isAborted)//These steps should be redundant, but just in case
        {
            if (!steps.get(currentStepIndex).isComplete())
            {
                steps.get(currentStepIndex).execute();
            }
            else if (!isAborted)//That means it completed, but didn't abort
            {
                steps.get(currentStepIndex).stop();
                currentStepIndex++;

                if (currentStepIndex>=steps.size())
                {
                    isRunning=false;
                }
                else
                {
                    execute();//call yourself recursively in order to take care of the possibility that the next leg is automatically complete
                    //in most cases, this will result in running the next leg
           
                }
            }
            else  //if aborted
            {
                //Do nothing
            }
            
        }
        //If you put anything in this area, don't forget that this could be called multiple times druing execute, due to recursive call

        
    }


    public void addStep(DriverlessStep nextStep)
    {
        steps.add(nextStep);
    }


}
