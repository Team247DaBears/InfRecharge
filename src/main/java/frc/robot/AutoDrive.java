package frc.robot;

import com.revrobotics.ControlType;

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
                Devices.frontLeft.setPosition(0);
                Devices.frontRight.setPosition(0);
            }
            case Drive: {
                double frontLeftPos = Devices.frontLeft.getPosition();
                //double backLeftPos = Devices.backLeft.getPosition();

                double frontRightPos = Devices.frontRight.getPosition();
                //double backRightPos = Devices.backRight.getPosition();

                double leftDiff = q.LeftDrivePos - frontLeftPos;
                double rightDiff = q.RightDrivePos - frontRightPos;
                if (leftDiff > .2 || rightDiff > .2) {
                    Devices.frontLeft.setReference(q.LeftDrivePos, ControlType.kPosition);
                    Devices.frontRight.setReference(q.RightDrivePos, ControlType.kPosition);

                    // not sure the following are needed with PID SparkMax Example doesn't have
                    Devices.frontLeft.set(q.LeftDriveSpeed); // set the left speed
                    Devices.frontRight.set(q.RightDriveSpeed); // set the right speed
                    switch (q.gearState) {
                        case HighGearOff:
                        case HighGearPressed:{
                            Devices.gearShift.set(true); // set High speed           
                        }
                        case LowGearOff:
                        case LowGearPressed: {
                            Devices.gearShift.set(false); // set low speed           
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
            Devices.gearShift.set(false); // set low speed   
            Devices.backLeft.Position = 0;            
            Devices.frontLeft.Position = 0;            
            Devices.backRight.Position = 0;            
            Devices.frontLeft.Position = 0;            
        }
    }
    public static void autonomousModeInit(){
        Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        Devices.backRight.setFollower(Devices.frontRight); // set follower speed
        AutoQueue.clearQueue();  
        // drive forward for 2ft
        AutoQueue.addDriveQueue(AutoStates.Drive,
            DriveStates.DriveStart,
            GearStates.LowGearOff,
            .1,10.0, /*LeftDrivePos,LeftDriveSpeed*/
            .1,10.0 /*RightDrivePos,RightDriveSpeed*/);
        AutoQueue.addDriveQueue(AutoStates.Drive,
            DriveStates.DriveStart,
            GearStates.LowGearOff,
            -.3,10.0, /*LeftDrivePos,LeftDriveSpeed*/
            -.1,10.0 /*RightDrivePos,RightDriveSpeed*/);
//        AutoQueue.addTargetQueue(AutoStates.Target,TargetStates.TargetStart,2);
      }    
}       