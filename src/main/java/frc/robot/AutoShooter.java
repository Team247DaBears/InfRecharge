package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import frc.robot.Devices;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.cscore.CvSource;

import com.kylecorry.frc.vision.Range;
import com.kylecorry.frc.vision.camera.CameraSettings;
import com.kylecorry.frc.vision.camera.FOV;
import com.kylecorry.frc.vision.camera.Resolution;
import com.kylecorry.frc.vision.contourFilters.ContourFilter;
import com.kylecorry.frc.vision.contourFilters.StandardContourFilter;
import com.kylecorry.frc.vision.distance.AreaCameraDistanceEstimator;
import com.kylecorry.frc.vision.distance.FixedAngleCameraDistanceEstimator;
import com.kylecorry.frc.vision.filters.HSVFilter;
import com.kylecorry.frc.vision.filters.TargetFilter;
import com.kylecorry.frc.vision.targetConverters.TargetGrouping;
import com.kylecorry.frc.vision.targeting.Target;
import com.kylecorry.frc.vision.targeting.TargetFinder;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class AutoShooter {
    // drive settings
    private final static double driveKP = 60; // sets the speed error between current and future
    private final static double driveKI = 0;
    private static final double driveKD = 0;
    private static final double driveMAXOUT = .4;
    private static final double driveMINOUT = -.4;
    private final static double driveFFVALUE = 0; // Will require experimentation to set a better value
    private static final double driveIZONE = 200;
    private static final double driveTARGETRPM = -1000; // Will begin with a single setpoint. We'll modify that for
                                                        // multiple

    // Shooter Settings
    private static double CONVEYORSPEED = -0.55;
    private static double CONVEYORSHOOTSPEED = -1;
    private static double FEEDER_HOLD_SPEED = -1.0;
    private static double FEEDER_FEED_SPEED = 1.0;

    private static SpeedController feeder;
    private static SpeedController conveyor;

    private static CANSparkMax shooter;
    // private CANEncoder encoder;
    // private CANPIDController controlLoop;

    private static Solenoid shooterSolenoid;

    private static final double LOW_DISTANCE = 10;
    private static final double HIGH_DISTANCE = 15;
    private static final double LOW_SPEED = -1250;
    private static final double HIGH_SPEED = -1450;
    private static final double CUTOFFRPM = -10; // diff between target and cutoff 
    private static final double TARGETRPM = -1300;
    public static long autoBeginTime = 0;
    private static boolean shooterSet = false;

    // Parameteres for velocity control PID on SparkMax
    private static final double KP = 0.1;
    private static final double KI = 2e-6;
    private static final double KD = 0;
    private static final double MAXOUT = 0;
    private static final double MINOUT = -1; // Never run backwards
    // It would be logical to use setInverted, and just use positive integers.
    // As logical as that would be, I'm not going to do it immediately, because
    // I have it from others that the encoders are inverted, and that might not work
    // with this. The solution would be to modify DaBearsSpeedController to invert
    // the encoder
    // if using setInverted, but that will require some testing.
    private static final double FFVALUE = -0; // Will require experimentation to set a better value
    private static final double IZONE = 200;

    private final static long AUTO_SHOOT_TIME_MS = 5600;// Amount of time to run the shooter for basic autonomous

    // Targeting
    static CameraStream camerastream;
    static int cnt = 0;
    static double distance;
    static double height;
    static double width;
    static double horizontal;
    static double virtical;
    static final double okShooter = 0.3; // ok shooter final distance diff
    static final double okDist = 0.1; // ok distance diff
    static final double okVirt = 0.1; // ok Vertical diff
    static final double okHztl = 0.1; // ok Horizontal diff

    static final double hztlAdj = 1; // adjust horizontal position ONE INCH
    static final double virtAdj = 1; // adjust horizontal position ONE INCH
    static final double default_distance = 8.0; // top centered target
    static final double default_height = 0.0; // top centered target
    static final double default_width = 0.0; // top centered target
    static final double default_horizontal = 0.0; // target location
    static final double default_virtical = 10; // target location

    static CvSource outputStream;
    private static Boolean isRunningTest = null;

    private final static Double CAMERA_CENTER_ADJUST = 4.0; // adjustment for camera location on robot

    private static final Double SHOOTER_ANGLE_STEPS = 28.0; // inches for 360 turn
    private static final Double SHOOTER_DRIVE_SPEED = .5; // Drive speed for Shooter

    public static void Init(CameraStream cameraStream) {
        isRunningTest();
        feeder = Devices.feeder;
        conveyor = Devices.conveyor;
        shooter = Devices.shooterSpark;
        shooterSolenoid = Devices.shooterAngleControl;
        shooterSet = false;
        camerastream = cameraStream;
    }

    public static void AutoShoot() {
        long elapsed;
        if (AutoQueue.getSize() == 0) {
            return;
        } // keep from having Auto overlap with TeleOpt
        AutoControlData q = AutoQueue.currentQueue();
        if (q.autoState != AutoStates.Shooter) {
            return;
        } // keep from having Auto overlap with TeleOpt
        if (q.WriteLog) {
            System.out.println(q.toString());
            // q.WriteLog = false;
        }
        switch (q.autoShooterState) {
        case shooterIDLE:
            // do nothing when shooter is IDLE
            break;

            case shooterTarget:
            shooter.getPIDController().setP(KP);
            shooter.getPIDController().setD(KD);
            shooter.getPIDController().setI(KI);
            shooter.getPIDController().setOutputRange(MINOUT, MAXOUT);
            shooter.getPIDController().setIZone(IZONE);
            shooter.getPIDController().setFF(FFVALUE / TARGETRPM); // approximate TARGET RPM
            shooter.getPIDController().setReference(0, ControlType.kDutyCycle);

            InitEncoderController(Devices.frontLeftSpark); // init drive motors
            InitEncoderController(Devices.frontRightSpark); // init drive motors
            InitEncoderController(Devices.backLeftSpark); // init drive motors
            InitEncoderController(Devices.backRightSpark); // init drive motors
            cnt = 0;

            case shooterFindTarget:
            WriteShooterStatus("AutoShooter-target");
            if (cnt++ > 3) { // give up and fire
                q.autoShooterState = AutoShooterStates.shooterSTART_RAMP; // check again
            }
            else {
                WriteShooterStatus("AutoShooter-target get target");
                autoBeginTime = System.currentTimeMillis();
                shooter.getPIDController().setReference(0, ControlType.kDutyCycle);
                conveyor.set(CONVEYORSPEED);
                feeder.set(FEEDER_HOLD_SPEED);
                Devices.frontLeftEncoder.setPosition(0);
                Devices.frontRightEncoder.setPosition(0);
                Devices.backLeftEncoder.setPosition(0);
                Devices.backRightEncoder.setPosition(0);

                Mat image = camerastream.getHighImage();
                if (image == null) {
                    WriteShooterStatus("TeleOpt-image null");
                    q.autoShooterState = AutoShooterStates.shooterFINISHED;
                } // image null (not set)
                else if (image.height() == 0) {
                    WriteShooterStatus("TeleOpt-image invalid");
                    q.autoShooterState = AutoShooterStates.shooterFINISHED;
                } // image empty
                else {
                    WriteShooterStatus("AutoShooter-target");
                    Target current = detectTopTarget(image);
                    if (current == null) {
                        WriteShooterStatus("TeleOpt-lost target");
                        q.autoShooterState = AutoShooterStates.shooterFINISHED;
                    } // no target found
                    else {
                        WriteShooterStatus("AutoShooter-target get dist");
                        SaveTargetImage("shooterTarget" + (cnt++), current, image);

                        AreaCameraDistanceEstimator distanceEstimator = new AreaCameraDistanceEstimator(
                                new AreaCameraDistanceEstimator.AreaDistancePair(100, 0),
                                new AreaCameraDistanceEstimator.AreaDistancePair(50, 20));
                        Double currdistance = Math.abs(distanceEstimator.getDistance(current));
                        // FixedAngleCameraDistanceEstimator distanceEstimator2 =
                        // new FixedAngleCameraDistanceEstimator(10, 7, 10);
                        // currdistance = Math.abs(distanceEstimator2.getDistance(current));
                        Double currhorz = current.getHorizontalAngle() - CAMERA_CENTER_ADJUST;
                        // TODO: adjust angle here
                        q.TargetDriveAngle = currhorz; // placeholder for target
                        q.CurrentDriveAngle = currhorz; // save current angle so angles can be adjusted
                        q.LeftDrivePos = -(SHOOTER_ANGLE_STEPS * (currhorz / 100)/2); // Approximate Angle change
                        q.RightDrivePos = (SHOOTER_ANGLE_STEPS * (currhorz / 100)/2); // adjust
                                                                                                                    // robot
                        q.LeftDriveSpeed = SHOOTER_DRIVE_SPEED;
                        q.RightDriveSpeed = SHOOTER_DRIVE_SPEED;
                        WriteShooterStatus("AutoTarget-Targ " + q.LeftDrivePos);

                        q.shooterDistance = currdistance;
                        if (Math.abs(currhorz) < okShooter) {
                            q.autoShooterState = AutoShooterStates.shooterSTART_RAMP;
                        }
                        else {
                            q.autoShooterState = AutoShooterStates.DriveDriving;
                        }
                    }
                }
            }
            break;
        case DriveDriving: {
            double frontLeftPos = Devices.frontLeftEncoder.getPosition();
            double frontRightPos = Devices.frontRightEncoder.getPosition();

            double leftDiff = java.lang.Math.abs(q.LeftDrivePos - frontLeftPos);
            double rightDiff = java.lang.Math.abs(q.RightDrivePos - frontRightPos);
            // TODO: adjust angle here
            WriteShooterStatus("AutoShooter-driv " +  leftDiff);
            if (leftDiff > .32 || rightDiff > .32) {
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
                StopAutoDrive();
                q.autoShooterState = AutoShooterStates.shooterFindTarget; // check again
            }
        }
        break;

        // Ramp up the flywheel
        case shooterSTART_RAMP:
            WriteShooterStatus("AutoShooter-ramp ");
            autoBeginTime = System.currentTimeMillis();
        // Ramp up the flywheel
        case shooterRAMPING_UP:
            elapsed = System.currentTimeMillis() - autoBeginTime;
            if (setPID(q.shooterDistance)) {
                q.autoShooterState = AutoShooterStates.shooterSHOOTING;
            }
            conveyor.set(CONVEYORSPEED);
            feeder.set(FEEDER_HOLD_SPEED);
            break;

        // Set Shooting Speed
        case shooterSHOOTING:
            elapsed = System.currentTimeMillis() - autoBeginTime;
            if (elapsed > AUTO_SHOOT_TIME_MS) {
                q.autoShooterState = AutoShooterStates.shooterFINISHED;
            }
            if (setPID(q.shooterDistance)) {
//                WriteShooterStatus("AutoShooter-Shoot");
                feeder.set(FEEDER_FEED_SPEED);
                conveyor.set(CONVEYORSHOOTSPEED); // shooter right speed, shoot!
            }
            else {
//                WriteShooterStatus("AutoShooter-Wait");
                feeder.set(FEEDER_HOLD_SPEED);
                conveyor.set(CONVEYORSPEED); // shooter too slow wait
            }
            break;

        // Shooting Completed
        case shooterFINISHED:
            WriteShooterStatus("AutoShooter-finished");
            Devices.frontLeft.set(0);
            Devices.frontRight.set(0);
            Devices.backLeft.set(0);
            Devices.backRight.set(0);
            shooter.getPIDController().setReference(0, ControlType.kDutyCycle);
            conveyor.set(0); // Will be changed if we move to something fancier
            feeder.set(0);
            AutoQueue.removeCurrent();
            break;

        // just in case an error occurs stop shooter all together~
        default:
        WriteShooterStatus("AutoShooter-finished");

            q.autoShooterState = AutoShooterStates.shooterFINISHED;
            break;
        }
    }

    // This is a work in progress
    public static boolean setPID(Double shooterDistance) {
        Double speed;
        Double Velocity = shooter.getEncoder().getVelocity();
   //     System.out.println("distance:"+shooterDistance);
            if (shooterDistance <= LOW_DISTANCE) {
                speed =  (shooterDistance/LOW_DISTANCE)*LOW_SPEED;
                if (!shooterSet) {
//                    System.out.println("lowSpeed:"+speed);
                    shooterSolenoid.set(true);
                    shooter.getPIDController().setReference(speed, ControlType.kVelocity);
                }
            }
            else {
                speed =  (shooterDistance/HIGH_DISTANCE)*HIGH_SPEED;
//                System.out.println("highSpeed:"+speed);
                if (!shooterSet) {
                    shooterSolenoid.set(false);
                    shooter.getPIDController().setReference(speed, ControlType.kVelocity);
                }
            }
            shooterSet = true;
//            System.out.println("lowSpeed:"+(speed-CUTOFFRPM));
            if (Velocity >= (speed-CUTOFFRPM)) { //reached shooting speed?
//                System.out.println("shoot!");
                return true;    // yes, shoot
            }
            else {
                return false; // no, wait till get to speed
            }
        }

    public static void StopAutoDrive() {
        Devices.backLeft.set(0); // stop wheels
        Devices.frontLeft.set(0); // stop wheels
        Devices.frontRight.set(0); // stop wheels
        Devices.backRight.set(0); // stop wheels
        Devices.gearShift.set(false); // set low speed
        Devices.setMotorConversionLow(); // set gear to low
        Devices.backLeftEncoder.setPosition(0); // set reference position to 0
        Devices.frontLeftEncoder.setPosition(0); // set reference position to 0
        Devices.backRightEncoder.setPosition(0); // set reference position to 0
        Devices.frontLeftEncoder.setPosition(0); // set reference position to 0
    }


    /**
     * Detects the 2020 top vision target.
     * 
     * @param image The image from the camera.
     * @return The list of vision target groups.
     */
    public static Target detectTopTarget(Mat image) {
        // Adjust these parameters for your team's needs
        Target foundTarget;

        if (image == null) {
            System.err.println("image null");
            return null;
        }
        if (image.height() == 0) {
            System.err.println("image invalid");
            return null;
        }

        // Hue/Sat/Value filter parameters
        Range hsvHue = new Range(56, 119);
        Range hsvSaturation = new Range(144, 255);
        Range hsvValue = new Range(37, 130);

        // Contour filter parameters
        Range area = new Range(10, 100);
        Range fullness = new Range(0, 400);
        Range aspectRatio = new Range(200, 400);

        area = new Range(0, 10000);
        fullness = new Range(0, 10000);
        aspectRatio = new Range(0, 10000);

        // Camera settings
        FOV fov = new FOV(50, 40); // This is the approx. Microsoft LifeCam FOV
        Resolution resolution = new Resolution(720, 478);
        boolean cameraInverted = false;

        int imageArea = resolution.getArea();

        // An HSV filter may be better for FRC target detection
        CameraSettings cameraSettings = new CameraSettings(cameraInverted, fov, resolution);
        // TargetFilter targetFilter = new HSVFilter(new Range(50, 70), new Range(100,
        // 255), new Range(100, 255));
        TargetFilter targetFilter = new HSVFilter(hsvHue, hsvSaturation, hsvValue);
        ContourFilter contourFilter = new StandardContourFilter(area, fullness, aspectRatio, imageArea);
        TargetGrouping targetGrouping = TargetGrouping.SINGLE;

        TargetFinder targetFinder = new TargetFinder(cameraSettings, targetFilter, contourFilter, targetGrouping);

        // Find targets
        List<Target> targets = targetFinder.findTargets(image);
        if (targets == null) {
            System.err.println("no targets found");
            return null;
        }
        if (targets.size() == 0) {
            System.err.println("no targets found");
            return null;
        }

        // If the current target is a left and the next is a right, make it a pair
        for (int i = 0; i < targets.size(); i++) {
            // System.out.println(":"+targets.get(i).toString());
            if (targets.get(i).getPercentArea() >= 1) {
                if (targets.get(i).getVerticalAngle() >= 10) {
                    // System.out.println("-->"+targets.get(i).toString());
                    foundTarget = targets.get(i);
                    SaveTargetImage("findTopTarget", foundTarget, image);
                    return targets.get(i);
                }
            }
        }

        return null;
    }

    public static void SaveTargetImage(String name, Target target, Mat image) {
        RotatedRect boundary = target.getBoundary();

        double height = boundary.boundingRect().height;
        double verticalPPI = height / 100;
        double holeYDist = 8.25;
        double centerY = boundary.center.y + holeYDist * verticalPPI;

        Imgproc.circle(image, new Point(boundary.center.x, centerY), boundary.boundingRect().width/2, new Scalar(255, 0, 255), 4);
        Imgproc.drawMarker(image, new Point(boundary.center.x, centerY), new Scalar(255, 0, 255), Core.TYPE_GENERAL, 30, 2);    

        // The following line is for desktop testing, use the CameraServer to display the image on a robot

        if (isRunningTest()) {
            Imgcodecs.imwrite("snap_" + name + ".jpg", image);
        } else {
            outputStream.putFrame(image);
        }
    }

    private static boolean isRunningTest() {
        if (isRunningTest == null) {
            isRunningTest = true;
            try {
                Class.forName("org.junit.Test");
            } catch (ClassNotFoundException e) {
                isRunningTest = false;
            }
        }
        return isRunningTest;
    }

    public static void WriteShooterStatus(String message) {
        if (isRunningTest) {
            System.out.println("Shooter:"+message);
        }
        else {
            System.out.println("-Shooter:"+message);
            SmartDashboard.putString("Shooter:", message);
        }
    }

    public static void InitEncoderController(CANSparkMax motor) {
        // motor.restoreFactoryDefaults();
        motor.set(0);
        motor.getEncoder().setPosition(0);
        motor.getPIDController().setP(driveKP);
        motor.getPIDController().setD(driveKD);
        motor.getPIDController().setI(driveKI);
        motor.getPIDController().setOutputRange(driveMINOUT, driveMAXOUT);
        motor.getPIDController().setIZone(driveIZONE);
        motor.getPIDController().setFF(driveFFVALUE / driveTARGETRPM);
        //motor.setReference(0, ControlType.kPosition);
    }

}       