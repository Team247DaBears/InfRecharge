/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.SerialPort;

/**
 * This class processes incoming serial data from the Jevois camera.
 * 
 * 
 */
public class SerialReceiver {
    //Protocol
    //All targets will be detected and sent a single line of serial, java characters, code.
    //The line will begin with a letter (a different one for each object type, followed by coordinates where the object is detected, separated by a single space)
    //The first example will be angle to target.  The serial data for that will look like this:

    //  A  NNN.NNN  ends with whitespace (line feed and/or carriage return, whatever python puts out)

    private SerialPort jevoisPort;  //Temporary.  Will eventually go in Devices
    String stAccumulatedString="";
    private serialReceiverStates currentState=serialReceiverStates.IDLE;
    private double receivedAngle;
    private long timeOfReceipt=0;
    private boolean newAngleReceived=false;

    

    public void processAvailableCharacters()
    {
        int charsAvailable=jevoisPort.getBytesReceived();
        if (charsAvailable>0)
        {
            
            byte[] chars=jevoisPort.read(charsAvailable);
            for (int i=0; i<charsAvailable;i++)
            {
                char newchar=(char) chars[i];
                switch(currentState)
                {
                    case IDLE: if (newchar=='A')
                                  currentState=serialReceiverStates.ANGLEDETECTED;
                                  break;
                    case ANGLEDETECTED: if (newchar==' ')
                                  {
                                      currentState=serialReceiverStates.RECEIVINGANGLE;
                                  }
                                  else
                                  {
                                    
                                    currentState=serialReceiverStates.IDLE;  //You got lost somewhere
                                  }
                                  break;
                    case RECEIVINGANGLE:
                                  if (newchar==' ')
                                  {
                                      try
                                      {
                                          receivedAngle=Double.parseDouble(stAccumulatedString);
                                          //This part will only happen if it doesn't throw an exception
                                          this.timeOfReceipt=System.currentTimeMillis();
                                          newAngleReceived=true;
                                          currentState=serialReceiverStates.IDLE;

                                      }
                                      catch(Exception ex)
                                      {
                                          stAccumulatedString="";
                                          currentState=serialReceiverStates.IDLE;
                                      }
                                  }
                                  else
                                  {
                                      stAccumulatedString+=newchar;
                                      if (!isLegalNumber(stAccumulatedString))
                                      {
                                          stAccumulatedString="";//abort
                                          currentState=serialReceiverStates.IDLE;
                                      }
                                  }
                                  break;
                    
                    
                }
            }
        }
    }

    private boolean isLegalNumber(String partialString)
    {
        if (partialString.equals("=")) return true;
        if (partialString.equals("+")) return true;
        try
        {
        if (partialString.endsWith(".")) partialString=partialString.substring(0,partialString.length()-2);
        Double.parseDouble(partialString);
        return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }

    public boolean isNewAngleAvailable()
    {
        return newAngleReceived;
    }

    public double getNewAngle()
    {
        newAngleReceived=false;
        return receivedAngle;
    }

    public long getAngleTime()
    {
        return timeOfReceipt;
    }


}
