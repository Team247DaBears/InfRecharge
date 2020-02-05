package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import frc.robot.Devices;
public class  AutoDrive
{
    public static void Drive()
      {
        AutoControlData q = AutoQueue.currentQueue();
        if (q.WriteLog ) {
            System.out.println(q.toString());
            //q.WriteLog = false;
        }
        switch (q.driveState) {
            case DriveStart: {
                Devices.frontLeft.set(q.LeftDriveSpeed); // set the left speed
                Devices.frontLeft.setFollower(Devices.backLeft); // set follower speed
                Devices.frontRight.set(q.RightDriveSpeed); // set the right speed
                Devices.frontRight.setFollower(Devices.backRight); // set follower speed

                Devices.frontLeft.setPosition(q.LeftDrivePos); // set the left speed
                Devices.frontLeft.setFollower(Devices.backLeft); // set follower speed
                Devices.frontRight.setPosition(q.RightDrivePos); // set the right speed
                Devices.frontRight.setFollower(Devices.backRight); // set follower speed
                q.driveState = DriveStates.Drive;
            }
            case Drive: {
                double frontLeftPos = Devices.frontLeft.getPosition();
                double backLeftPos = Devices.backLeft.getPosition();

                double frontRightPos = Devices.frontRight.getPosition();
                double backRightPos = Devices.backRight.getPosition();
                
                if (frontLeftPos!=0 && frontRightPos!=0) {
                    Devices.frontLeft.set(q.LeftDriveSpeed); // set the left speed
                    Devices.frontLeft.setFollower(Devices.backLeft); // set follower speed
                    Devices.frontRight.set(q.RightDriveSpeed); // set the right speed
                    Devices.frontRight.setFollower(Devices.backRight); // set follower speed
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
                        System.out.println("AutoStop");
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
        // update the position when no encoder used
        Devices.frontLeft.updatePosition(); // set the left speed
        Devices.backLeft.updatePosition(); // set follower speed
        Devices.frontRight.updatePosition(); // set the right speed
        Devices.backRight.updatePosition(); // set follower speed
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
            Devices.backLeft.Position = 0;            
            Devices.frontLeft.Position = 0;            
            Devices.backRight.Position = 0;            
            Devices.frontLeft.Position = 0;            
        }
    }
    public static void autonomousModeInit(){
        AutoQueue.clearQueue();
        AutoControlData q=new AutoControlData();      
  
        // drive backward for 2ft
        q.autoState = AutoStates.Drive;
        q.driveState = DriveStates.DriveStart;
        q.gearState = GearStates.LowGearPressed;
        q.LeftDrivePos=250;
        q.LeftDriveSpeed=-.5;
        q.RightDrivePos=250;
        q.RightDriveSpeed=-.5;
        AutoQueue.addQueue(q);
      }    
}       