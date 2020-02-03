package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import frc.robot.Devices;
public class  AutoDrive
{

    public static void Drive()
      {
        AutoControlData q = AutoQueue.currentQueue();
        if (q.WriteLog) {
            System.out.println(q.toString());
            q.WriteLog = false;
        }
        switch (q.driveState) {
            case Drive: {
                q.LeftDriveCount = q.LeftDriveCount - 1; // TODO: replace with calc from sensor
                q.RightDriveCount = q.RightDriveCount - 1; // TODO: replace with calc from sensor
                // TODO: Compare to Count to see if it is time to stop
                if (q.LeftDriveCount>0 | q.RightDriveCount >0) {
                    Devices.backLeft.set(q.LeftDriveSpeed); // stop wheels
                    Devices.frontLeft.set(q.LeftDriveSpeed); // stop wheels
                    Devices.frontRight.set(q.RightDriveSpeed); // stop wheels
                    Devices.backRight.set(q.RightDriveSpeed); // stop wheels
                    switch (q.gearState) {
                        case HighGearOff:
                        case HighGearPressed:{
                            Devices.gearShift.set(DoubleSolenoid.Value.kForward); // set High speed           
                        }
                        case LowGearOff:
                        case LowGearPressed: {
                            Devices.gearShift.set(DoubleSolenoid.Value.kReverse); // set low speed           
                        }
                    }
                }
                else {
                    AutoQueue.removeCurrent();
                    q = AutoQueue.currentQueue();
                    if (q.autoState != AutoStates.Drive) {
                        StopAutoDrive();
                    }
                }
                break;
            }
            case Stop: {
                StopAutoDrive();
                break;            
            }
        }
      }

    public static void StopAutoDrive(boolean force) {
        if (force) {
            AutoQueue.clearQueue();
        }
        StopAutoDrive();
    }    

    public static void StopAutoDrive() {
        AutoControlData q = AutoQueue.currentQueue();
        if ( q.autoState != AutoStates.Drive) {
            Devices.backLeft.set(0); // stop wheels
            Devices.frontLeft.set(0); // stop wheels
            Devices.frontRight.set(0); // stop wheels
            Devices.backRight.set(0); // stop wheels
            Devices.gearShift.set(DoubleSolenoid.Value.kReverse); // set low speed               
        }
    }    
}       