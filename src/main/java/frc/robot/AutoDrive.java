package frc.robot;

import com.revrobotics.ControlType;
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
        if (AutoQueue.getSize() ==0) {return;} // keep from having Auto overlap with TeleOpt
        AutoControlData q = AutoQueue.currentQueue();
        if (q.autoState != AutoStates.AutoDrive) {return;} // keep from having Auto overlap with TeleOpt
        if (q.WriteLog) {
            System.out.println(q.toString());
            // q.WriteLog = false;
        }
        switch (q.driveState) {
        case DriveStart: {
            if (q.TargetDriveAngle==999.999 && (q.LeftDriveSpeed == q.RightDriveSpeed)) {
                // TODO: set drive angle to ensure bumped robots drive to same angle as set 
            }
            q.driveState = DriveStates.Drive;
            Devices.frontLeftEncoder.setPosition(0);
            Devices.frontRightEncoder.setPosition(0);
            Devices.backLeftEncoder.setPosition(0);
            Devices.backRightEncoder.setPosition(0);
            //System.out.println("getPosition:" + Devices.frontLeftEncoder.getPosition());

            Devices.frontLeftPID.setOutputRange(-1 * Math.abs(q.LeftDriveSpeed), Math.abs(q.LeftDriveSpeed));
            Devices.frontRightPID.setOutputRange(-1 * Math.abs(q.RightDriveSpeed), Math.abs(q.RightDriveSpeed));
            Devices.backLeftPID.setOutputRange(-1 * Math.abs(q.LeftDriveSpeed), Math.abs(q.LeftDriveSpeed));
            Devices.backRightPID.setOutputRange(-1 * Math.abs(q.RightDriveSpeed), Math.abs(q.RightDriveSpeed));
            //System.out.println("min:" + -1 * Math.abs(q.LeftDriveSpeed) + " Max:" + Math.abs(q.LeftDriveSpeed));
        }
        case Drive: {
            double frontLeftPos = Devices.frontLeftEncoder.getPosition();
            double frontRightPos = Devices.frontRightEncoder.getPosition();

            double leftDiff = java.lang.Math.abs(q.LeftDrivePos - frontLeftPos);
            double rightDiff = java.lang.Math.abs(q.RightDrivePos - frontRightPos);
            //System.out.println("diff:" + leftDiff);
            //System.out.println("diff:" + rightDiff);
            if (leftDiff > .2 || rightDiff > .2) {
                Devices.frontLeftPID.setReference(q.LeftDrivePos, ControlType.kPosition);
                Devices.frontRightPID.setReference(q.RightDrivePos, ControlType.kPosition);
                Devices.backLeftPID.setReference(q.LeftDrivePos, ControlType.kPosition);
                Devices.backRightPID.setReference(q.RightDrivePos, ControlType.kPosition);

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
                if (q.autoState != AutoStates.AutoDrive) {
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
        if (q.autoState != AutoStates.AutoDrive) {
            Devices.backLeft.set(0); // stop wheels
            Devices.frontLeft.set(0); // stop wheels
            Devices.frontRight.set(0); // stop wheels
            Devices.backRight.set(0); // stop wheels
            Devices.gearShift.set(false); // set low speed
            Devices.backLeftEncoder.setPosition(0);
            Devices.frontLeftEncoder.setPosition(0);
            Devices.backRightEncoder.setPosition(0);
            Devices.frontLeftEncoder.setPosition(0);
        }
    }


    public static void autonomousInitBackupLeft() {
        InitEncoderController(Devices.frontLeftSpark);
        InitEncoderController(Devices.frontRightSpark);
        InitEncoderController(Devices.backLeftSpark);
        InitEncoderController(Devices.backRightSpark);
        // Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        // Devices.backRight.setFollower(Devices.frontRight); // set follower speed

        AutoQueue.clearQueue();
        AutoQueue.addShooterQueue(AutoStates.Shooter, ShootingStates.IDLE, 10.0);
        AutoQueue.addDriveQueue(.4, -8.0, .4, 5.0 /* turn toward color wheel */);
        AutoQueue.addDriveQueue(.2, 30.0, .2, 30.0 /* drive toward color wheel */);
        AutoQueue.addDriveQueue(.2, -60.0, .2, -60.0 /* drive toward color wheel */);
        }

    public static void autonomousInitBackupRight() {
        InitEncoderController(Devices.frontLeftSpark);
        InitEncoderController(Devices.frontRightSpark);
        InitEncoderController(Devices.backLeftSpark);
        InitEncoderController(Devices.backRightSpark);
        // Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        // Devices.backRight.setFollower(Devices.frontRight); // set follower speed

        AutoQueue.clearQueue();
        AutoQueue.addShooterQueue(AutoStates.Shooter, ShootingStates.IDLE, 10.0);
        AutoQueue.addDriveQueue(.4, 5.0, .4, -8.0 /* turn toward color wheel */);
        AutoQueue.addDriveQueue(.2, 30.0, .2, 30.0 /* drive toward color wheel */);
        AutoQueue.addDriveQueue(.2, -60.0, .2, -60.0 /* drive toward color wheel */);
        }

    public static void autonomousInitRight() {
        InitEncoderController(Devices.frontLeftSpark);
        InitEncoderController(Devices.frontRightSpark);
        InitEncoderController(Devices.backLeftSpark);
        InitEncoderController(Devices.backRightSpark);
        // Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        // Devices.backRight.setFollower(Devices.frontRight); // set follower speed

        AutoQueue.clearQueue();
        // drive forward for 2ft
        AutoQueue.addShooterQueue(AutoStates.Shooter, ShootingStates.IDLE, 10.0);
        AutoQueue.addDriveQueue(.4, 40.0, .4, -40.0 /* turn toward color wheel */);
        AutoQueue.addDriveQueue(.4, 20.0, .4, 20.0 /* drive toward color wheel */);
    }

    public static void autonomousInitCenter() {
        InitEncoderController(Devices.frontLeftSpark);
        InitEncoderController(Devices.frontRightSpark);
        InitEncoderController(Devices.backLeftSpark);
        InitEncoderController(Devices.backRightSpark);
        // Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        // Devices.backRight.setFollower(Devices.frontRight); // set follower speed

        AutoQueue.clearQueue();
        // drive forward for 2ft
        AutoQueue.addShooterQueue(AutoStates.Shooter, ShootingStates.IDLE, 10.0);
        // AutoQueue.addTargetQueue(AutoStates.Target,TargetStates.TargetStart1,2);
        AutoQueue.addDriveQueue(.4, -10.0, .4, -10.0 /* backup from line */);
        AutoQueue.addDriveQueue(.4, 28.0, .4, -28.0 /* turn toward color wheel */);
        AutoQueue.addDriveQueue(.4, 62.0, .4, 62.0 /* drive toward color wheel */);
        AutoQueue.addDriveQueue(.4, 14.0, .4, -14.0 /* Turn toward balls */);
        //AutoQueue.addIntakeQueue(AutoStates.Intake,IntakeStates.intakeRun);
        // AutoQueue.addDriveQueue(AutoStates.Drive,
        // DriveStates.DriveStart,
        // GearStates.LowGearPressed,
        // -.3,10.0, /*LeftDrivePos,LeftDriveSpeed*/
        // -.1,10.0 /*RightDrivePos,RightDriveSpeed*/);
    }

    public static void autonomousInitCenterDelay() {
        InitEncoderController(Devices.frontLeftSpark);
        InitEncoderController(Devices.frontRightSpark);
        InitEncoderController(Devices.backLeftSpark);
        InitEncoderController(Devices.backRightSpark);
        // Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        // Devices.backRight.setFollower(Devices.frontRight); // set follower speed

        AutoQueue.clearQueue();
        // drive forward for 2ft
        AutoQueue.addDriveQueue(.2, 20.0, .2, 20.0 /* backup from line */);
        AutoQueue.addDriveQueue(.2, -20.0, .2, -20.0 /* backup from line */);
        AutoQueue.addShooterQueue(AutoStates.Shooter, ShootingStates.IDLE, 10.0);
        // AutoQueue.addTargetQueue(AutoStates.Target,TargetStates.TargetStart1,2);
        AutoQueue.addDriveQueue(.4, -10.0, .4, -10.0 /* backup from line */);
//        AutoQueue.addDriveQueue(.4, 28.0, .4, -28.0 /* turn toward color wheel */);
//        AutoQueue.addDriveQueue(.4, 62.0, .4, 62.0 /* drive toward color wheel */);
//        AutoQueue.addDriveQueue(.4, 14.0, .4, -14.0 /* Turn toward balls */);
        //AutoQueue.addIntakeQueue(AutoStates.Intake,IntakeStates.intakeRun);
        // AutoQueue.addDriveQueue(AutoStates.Drive,
        // DriveStates.DriveStart,
        // GearStates.LowGearPressed,
        // -.3,10.0, /*LeftDrivePos,LeftDriveSpeed*/
        // -.1,10.0 /*RightDrivePos,RightDriveSpeed*/);
    }

    public static void autonomousInitLeft() {
        InitEncoderController(Devices.frontLeftSpark);
        InitEncoderController(Devices.frontRightSpark);
        InitEncoderController(Devices.backLeftSpark);
        InitEncoderController(Devices.backRightSpark);
        // Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        // Devices.backRight.setFollower(Devices.frontRight); // set follower speed

        AutoQueue.clearQueue();
        // drive forward for 2ft
        AutoQueue.addShooterQueue(AutoStates.Shooter, ShootingStates.IDLE, 10.0);
        // AutoQueue.addTargetQueue(AutoStates.Target,TargetStates.TargetStart1,2);
        AutoQueue.addDriveQueue(.4, -10.0, .4, -10.0 /* backup from line */);
        AutoQueue.addDriveQueue(.4, 28.0, .4, -28.0 /* turn toward color wheel */);
        AutoQueue.addDriveQueue(.4, 62.0, .4, 62.0 /* drive toward color wheel */);
        AutoQueue.addDriveQueue(.4, 14.0, .4, -14.0 /* Turn toward balls */);
        //AutoQueue.addIntakeQueue(AutoStates.Intake,IntakeStates.intakeRun);
        // AutoQueue.addDriveQueue(AutoStates.Drive,
        // DriveStates.DriveStart,
        // GearStates.LowGearPressed,
        // -.3,10.0, /*LeftDrivePos,LeftDriveSpeed*/
        // -.1,10.0 /*RightDrivePos,RightDriveSpeed*/);
    }

    public static void autonomousInitDefault() {
        InitEncoderController(Devices.frontLeftSpark);
        InitEncoderController(Devices.frontRightSpark);
        InitEncoderController(Devices.backLeftSpark);
        InitEncoderController(Devices.backRightSpark);
        // Devices.backLeft.setFollower(Devices.frontLeft); // set follower speed
        // Devices.backRight.setFollower(Devices.frontRight); // set follower speed

        AutoQueue.clearQueue();
        // drive forward for 2ft
        AutoQueue.addShooterQueue(AutoStates.Shooter, ShootingStates.IDLE, 10.0);
        // AutoQueue.addTargetQueue(AutoStates.Target,TargetStates.TargetStart1,2);
        AutoQueue.addDriveQueue(.4, -10.0, .4, -10.0 /* backup from line */);
        AutoQueue.addDriveQueue(.4, 26.0, .4, -26.0 /* turn toward color wheel */);
        AutoQueue.addDriveQueue(.4, 66.0, .4, 66.0 /* drive toward color wheel */);
        AutoQueue.addDriveQueue(.4, 11.5, .4, -11.5 /* Turn toward balls */);
        //AutoQueue.addIntakeQueue(AutoStates.Intake,IntakeStates.intakeRun);
        // AutoQueue.addDriveQueue(AutoStates.Drive,
        // DriveStates.DriveStart,
        // GearStates.LowGearPressed,
        // -.3,10.0, /*LeftDrivePos,LeftDriveSpeed*/
        // -.1,10.0 /*RightDrivePos,RightDriveSpeed*/);
    }
    public static void InitEncoderController(Object Spark) {
        // motor.restoreFactoryDefaults();
        Devices.InitEncoderController(Spark,KP,KD,KI,MINOUT,MAXOUT,IZONE,FFVALUE,TARGETRPM,0,0);
    }

}       