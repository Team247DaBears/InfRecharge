package frc.robot;

import com.revrobotics.ControlType;
import java.lang.Math.*;
import frc.robot.Devices;
public class  AutoDrive
{
    private final static double KP = 60; // sets the speed error between current and future
    private final static double KI = 0;
    private static final double KD = 0;
    private static final double MAXOUT = .4;
    private static final double MINOUT = -.4;
    private final static double FFVALUE = 0; // Will require experimentation to set a better value
    private static final double IZONE = 200;
    private static final double TARGETRPM = -1000; // Will begin with a single setpoint. We'll modify that for multiple
                                                   // distance ranges later.

    public static void Drive() {
        if (AutoQueue.getSize() ==0) {return;}
        AutoControlData q = AutoQueue.currentQueue();
        if (q.WriteLog) {
            System.out.println(q.toString());
            // q.WriteLog = false;
        }
        switch (q.driveState) {
        case DriveStart: {
            q.driveState = DriveStates.Drive;
            Devices.frontLeft.setPosition(0);
            Devices.frontRight.setPosition(0);
            Devices.backLeft.setPosition(0);
            Devices.backRight.setPosition(0);
            //System.out.println("getPosition:" + Devices.frontLeft.getPosition());

            Devices.frontLeft.setOutputRange(-1 * Math.abs(q.LeftDriveSpeed), Math.abs(q.LeftDriveSpeed));
            Devices.frontRight.setOutputRange(-1 * Math.abs(q.RightDriveSpeed), Math.abs(q.RightDriveSpeed));
            Devices.backLeft.setOutputRange(-1 * Math.abs(q.LeftDriveSpeed), Math.abs(q.LeftDriveSpeed));
            Devices.backRight.setOutputRange(-1 * Math.abs(q.RightDriveSpeed), Math.abs(q.RightDriveSpeed));
            //System.out.println("min:" + -1 * Math.abs(q.LeftDriveSpeed) + " Max:" + Math.abs(q.LeftDriveSpeed));
        }
        case Drive: {
            double frontLeftPos = Devices.frontLeft.getPosition();
            double frontRightPos = Devices.frontRight.getPosition();

            double leftDiff = java.lang.Math.abs(q.LeftDrivePos - frontLeftPos);
            double rightDiff = java.lang.Math.abs(q.RightDrivePos - frontRightPos);
            //System.out.println("diff:" + leftDiff);
            //System.out.println("diff:" + rightDiff);
            if (leftDiff > .2 || rightDiff > .2) {
                Devices.frontLeft.setReference(q.LeftDrivePos, ControlType.kPosition);
                Devices.frontRight.setReference(q.RightDrivePos, ControlType.kPosition);
                Devices.backLeft.setReference(q.LeftDrivePos, ControlType.kPosition);
                Devices.backRight.setReference(q.RightDrivePos, ControlType.kPosition);

                // not sure the following are needed with PID SparkMax Example doesn't have
                // Devices.frontLeft.set(q.LeftDriveSpeed); // set the left speed
                // Devices.frontRight.set(q.RightDriveSpeed); // set the right speed
                switch (q.gearState) {
                case HighGearOff:
                case HighGearPressed: {
                    Devices.gearShift.set(true); // set High speed
                    Devices.setMotorConversionHigh();

                }
                case LowGearOff:
                case LowGearPressed: {
                    Devices.gearShift.set(false); // set low speed
                    Devices.setMotorConversionLow();
                }
                }
            } else {
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
        if (q.autoState != AutoStates.Drive) {
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

    public static void autonomousInitCenter() {
        InitEncoderController(Devices.frontLeft);
        InitEncoderController(Devices.frontRight);
        InitEncoderController(Devices.backLeft);
        InitEncoderController(Devices.backRight);
        // Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        // Devices.backRight.setFollower(Devices.frontRight); // set follower speed

        AutoQueue.clearQueue();
        // drive forward for 2ft
        AutoQueue.addDriveQueue(AutoStates.Drive, 
        DriveStates.DriveStart, 
        GearStates.LowGearPressed, 
        .3, 10.0, .3, 10.0 /* RightDrivePos,RightDriveSpeed */);
        AutoQueue.addDriveQueue(AutoStates.Drive, DriveStates.DriveStart, GearStates.LowGearPressed, -.3, 10.0, /*
                                                                                                                 * LeftDrivePos
                                                                                                                 * ,
                                                                                                                 * LeftDriveSpeed
                                                                                                                 */
                -.1, 10.0 /* RightDrivePos,RightDriveSpeed */);
        // AutoQueue.addTargetQueue(AutoStates.Target,TargetStates.TargetStart,2);
    }

    public static void autonomousModeInit() {
        InitEncoderController(Devices.frontLeft);
        InitEncoderController(Devices.frontRight);
        InitEncoderController(Devices.backLeft);
        InitEncoderController(Devices.backRight);
        // Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        // Devices.backRight.setFollower(Devices.frontRight); // set follower speed

        AutoQueue.clearQueue();
        // drive forward for 2ft
        AutoQueue.addShooterQueue(AutoStates.Shooter, ShootingStates.IDLE, 10.0);
        // AutoQueue.addTargetQueue(AutoStates.Target,TargetStates.TargetStart1,2);
        AutoQueue.addDriveQueue(AutoStates.Drive, DriveStates.DriveStart, GearStates.LowGearPressed, .4, -10.0, .4, -10.0 /* RightDrivePos,RightDriveSpeed */);
        AutoQueue.addDriveQueue(AutoStates.Drive, DriveStates.DriveStart, GearStates.LowGearPressed, .4, 29.0, .4, -29.0 /* RightDrivePos,RightDriveSpeed */);
        AutoQueue.addDriveQueue(AutoStates.Drive, DriveStates.DriveStart, GearStates.LowGearPressed, .4, 55.0, .4, 55.0 /* RightDrivePos,RightDriveSpeed */);
        AutoQueue.addDriveQueue(AutoStates.Drive, DriveStates.DriveStart, GearStates.LowGearPressed, .4, 14.0, .4, -14.0 /* RightDrivePos,RightDriveSpeed */);
//        AutoQueue.addDriveQueue(AutoStates.Drive, DriveStates.DriveStart, GearStates.LowGearPressed, 1.0, 5.0, 1.0, -5.0 /* RightDrivePos,RightDriveSpeed */);
//        AutoQueue.addDriveQueue(AutoStates.Drive, DriveStates.DriveStart, GearStates.LowGearPressed, 1.0, -10.0, 1.0, -10.0 /* RightDrivePos,RightDriveSpeed */);
        //AutoQueue.addIntakeQueue(AutoStates.Intake,IntakeStates.intakeRun);
        // AutoQueue.addDriveQueue(AutoStates.Drive,
        // DriveStates.DriveStart,
        // GearStates.LowGearPressed,
        // -.3,10.0, /*LeftDrivePos,LeftDriveSpeed*/
        // -.1,10.0 /*RightDrivePos,RightDriveSpeed*/);
    }

    public static void InitEncoderController(DaBearsSpeedController motor) {
        // motor.restoreFactoryDefaults();
        motor.set(0);
        motor.setPosition(0);
        motor.setP(KP);
        motor.setD(KD);
        motor.setI(KI);
        motor.setOutputRange(MINOUT, MAXOUT);
        motor.setIZone(IZONE);
        motor.setFF(FFVALUE / TARGETRPM);
        //motor.setReference(0, ControlType.kPosition);
    }
}       