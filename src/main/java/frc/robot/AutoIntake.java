/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.CvSource;

import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.kylecorry.frc.vision.distance.FixedAngleCameraDistanceEstimator;
//import com.kylecorry.frc.vision.distance.AreaCameraDistanceEstimator;
import com.kylecorry.frc.vision.targeting.Target;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;

import com.kylecorry.frc.vision.Range;
import com.kylecorry.frc.vision.camera.CameraSettings;
import com.kylecorry.frc.vision.camera.FOV;
import com.kylecorry.frc.vision.camera.Resolution;
import com.kylecorry.frc.vision.contourFilters.ContourFilter;
import com.kylecorry.frc.vision.contourFilters.StandardContourFilter;
import com.kylecorry.frc.vision.filters.HSVFilter;
import com.kylecorry.frc.vision.filters.TargetFilter;
import com.kylecorry.frc.vision.targetConverters.TargetGrouping;
import com.kylecorry.frc.vision.targeting.TargetFinder;

import java.util.List;

public class AutoIntake {
    static Boolean isRunningTest = null;
    DoubleSolenoid intakeSolenoid;
    private double CONVEYORSPEED = -0.60;
    CameraStream camerastream;
    CvSource outputStream;

    long cnt = 0;
    long tgt = 0;

    SpeedController intakeMotor;
    AutoIntakeStates intakeState;
    private final double INTAKE_DRIVE_SPEED = .5;
    private final double INTAKE_SPEED = .5;
    // Variables for AutoIntake
    private int BounceCount = 0; // counter to delay bounces
    private final Double CAMERA_CENTER_ADJUST = 4.0; // adjustment for camera location on robot
    private final int MAX_BOUNCE_COUNT = 1; // maximum delay before another bounce
    private final Double TARGET_CORRECT_DISTANCE = 4.0; // max distance to drive before target check
    private final Double SCOOP_DISTANCE = 10.0; // distance to just scoop up the ball
    private final Double RECHECK_TARGET = 1.0; // remaining distance before target check
    private final Double BALL_SIZE = 7.0; // remaining distance before target check
    // End of AutoIntake Specific variables

    private static final double KP = 5e-5;
    private static final double KI = 2e-6;
    private static final double KD = 0;
    private static final double MAXOUT = .5; // during intake drive speed
    private static final double MINOUT = -.5;
    private static final double FFVALUE = -0.22; // Will require experimentation to set a better value
    private static final double IZONE = 200;
    private static final double TARGETRPM = -1000; // Will begin with a single setpoint. We'll modify that for multiple
                                                   // distance ranges later.

    public void Init(CameraStream camerastream) {
        isRunningTest();
        WriteIntakeStatus("Teleopt");
        Devices.conveyor.set(0); // turn off conveyor
        intakeSolenoid = Devices.intake_solenoid;
        intakeMotor = Devices.intake_motor;
        this.camerastream = camerastream;
    }

    public void autoIntake(){
        if (AutoQueue.getSize() ==0) {return;}
        AutoControlData q = AutoQueue.currentQueue();
        if (q.autoState != AutoStates.AutoIntake) {
            return;
        } // keep from having Auto overlap with TeleOpt
        switch (q.autoIntakeState) {
            case intakeRun:
                WriteIntakeStatus("AutoIntake-Run");
                Devices.gearShift.set(false); // set low speed
                Devices.setMotorConversionLow();           

                Devices.conveyor.set(CONVEYORSPEED);
                intakeMotor.set(INTAKE_SPEED);
                q.autoIntakeState = AutoIntakeStates.intakeTarget;

                InitEncoderController(Devices.frontLeftSpark);
                InitEncoderController(Devices.frontRightSpark);
                InitEncoderController(Devices.backLeftSpark);
                InitEncoderController(Devices.backRightSpark);
                cnt = 0;

            break;
            case intakeTarget:
                if (cnt++>3) {
                    WriteIntakeStatus("TeleOpt-max Attempts");
                    q.autoIntakeState = AutoIntakeStates.intakeStop;
                } 
                Mat image = camerastream.getLowImage();
                if (image==null){
                    WriteIntakeStatus("TeleOpt-image null");
                    q.autoIntakeState = AutoIntakeStates.intakeStop;
                } // image null (not set)
                else if (image.height()==0){
                    WriteIntakeStatus("TeleOpt-image invalid");
                    q.autoIntakeState = AutoIntakeStates.intakeStop;
                } // image empty
                else {
                    WriteIntakeStatus("AutoIntake-target");
                    Target current = detectBallTarget(image);
                    if (current==null) {
                        WriteIntakeStatus("TeleOpt-lost target");
                        q.autoIntakeState = AutoIntakeStates.intakeStop;
                    } // no target found
                    else {
                        SaveTargetImage("ballTarget"+(cnt)+(tgt++), current, image);
                        //AreaCameraDistanceEstimator distanceEstimator = new AreaCameraDistanceEstimator(new AreaCameraDistanceEstimator.AreaDistancePair(100, 0), new AreaCameraDistanceEstimator.AreaDistancePair(50, 20));
                        //Double currdistance = Math.abs(distanceEstimator.getDistance(current));
                        //WriteIntakeStatus("AutoTarget-Targ "+currdistance);
                        FixedAngleCameraDistanceEstimator distanceEstimator2 = 
                            new FixedAngleCameraDistanceEstimator(0, 12, -27.7);
                        Double currdistance = Math.abs(distanceEstimator2.getDistance(current));
                        WriteIntakeStatus("AutoIntake-Targ "+currdistance);
                        Double currhorz = current.getHorizontalAngle() - CAMERA_CENTER_ADJUST;
                        if (currdistance < SCOOP_DISTANCE) { // Time to just scoop it up?
                            currdistance = SCOOP_DISTANCE;
                            q.autoIntakeState = AutoIntakeStates.intakeDown;    
                            currhorz = 0.0;        
                        }
                        else if (currdistance > TARGET_CORRECT_DISTANCE) { // set distance before next target correction
                            currdistance = TARGET_CORRECT_DISTANCE;
                            q.autoIntakeState = AutoIntakeStates.intakeDrive;
                        } 
                        q.LeftDrivePos = currdistance;
                        q.RightDrivePos = currdistance+(currdistance*(currhorz/100/TARGET_CORRECT_DISTANCE)); // adjust angle of robot
                        q.LeftDriveSpeed = INTAKE_DRIVE_SPEED;
                        q.RightDriveSpeed = INTAKE_DRIVE_SPEED;
                        Devices.frontLeftEncoder.setPosition(0);
                        Devices.frontRightEncoder.setPosition(0);
                        Devices.backLeftEncoder.setPosition(0);
                        Devices.backRightEncoder.setPosition(0);
                        Devices.frontLeftPID.setReference(q.LeftDrivePos, ControlType.kPosition);
                        Devices.frontRightPID.setReference(q.RightDrivePos, ControlType.kPosition);
                        Devices.backLeftPID.setReference(q.LeftDrivePos, ControlType.kPosition);
                        Devices.backRightPID.setReference(q.RightDrivePos, ControlType.kPosition);
                    }
                    intakeSolenoid.set(DoubleSolenoid.Value.kReverse); // Start with intake up
                }
                break;
            case intakeDrive:
                double frontLeftPos = Devices.frontLeftEncoder.getPosition();
                double frontRightPos = Devices.frontRightEncoder.getPosition();

                double leftDiff = java.lang.Math.abs(q.LeftDrivePos - frontLeftPos);
                double rightDiff = java.lang.Math.abs(q.RightDrivePos - frontRightPos);
                WriteIntakeStatus("AutoIntake-Driv "+leftDiff);
                if (leftDiff < RECHECK_TARGET && rightDiff < RECHECK_TARGET) { // time to reject target?
                    q.autoIntakeState = AutoIntakeStates.intakeTarget;
                }
                if (leftDiff > .2 || rightDiff > .2) {
                    // TODO: Add angle correction here
                    Devices.frontLeftPID.setReference(q.LeftDrivePos, ControlType.kPosition);
                    Devices.frontRightPID.setReference(q.RightDrivePos, ControlType.kPosition);
                    Devices.backLeftPID.setReference(q.LeftDrivePos, ControlType.kPosition);
                    Devices.backRightPID.setReference(q.RightDrivePos, ControlType.kPosition);
                }
                break;
            case intakeDown:
                //System.out.println("intakeDown: (bounce)");
                // bounce intake
            if (intakeSolenoid.get() == DoubleSolenoid.Value.kForward) {
                    intakeSolenoid.set(DoubleSolenoid.Value.kReverse); // raise intake
            }
            else {
                intakeSolenoid.set(DoubleSolenoid.Value.kForward); // lower intake
                q.autoIntakeState = AutoIntakeStates.intakeUp; // switch back to driving
            }
            // Intentual flow into IntakeUp... Allows skipping of bounce
        case intakeUp:
                frontLeftPos = Devices.frontLeftEncoder.getPosition();
                frontRightPos = Devices.frontRightEncoder.getPosition();

                leftDiff = java.lang.Math.abs(q.LeftDrivePos - frontLeftPos);
                rightDiff = java.lang.Math.abs(q.RightDrivePos - frontRightPos);
                WriteIntakeStatus("AutoIntake-Scop "+leftDiff);
                if (leftDiff > .2 || rightDiff > .2) {
                    //System.out.println("LeftDrivePos:" + leftDiff);
                    //System.out.println("RightDrivePos:" + rightDiff);
                    Devices.frontLeftPID.setReference(q.LeftDrivePos, ControlType.kPosition);
                    Devices.frontRightPID.setReference(q.RightDrivePos, ControlType.kPosition);
                    Devices.backLeftPID.setReference(q.LeftDrivePos, ControlType.kPosition);
                    Devices.backRightPID.setReference(q.RightDrivePos, ControlType.kPosition);
                    if (BounceCount++ > MAX_BOUNCE_COUNT) { // time for bounce again? 
                        BounceCount = 0;
                        q.autoIntakeState = AutoIntakeStates.intakeUp; // switch back to driving
                    }
                }
                else {
                    WriteIntakeStatus("AutoIntake-Ending");
                    intakeMotor.set(0);
                    q.autoIntakeState = AutoIntakeStates.intakeStop;
                    intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
                }
            break;
            case intakeStop:
                WriteIntakeStatus("Teleopt");
                intakeSolenoid.set(DoubleSolenoid.Value.kOff);
                AutoQueue.removeCurrent();
                System.out.println("queue size"+AutoQueue.getSize());
                break;    
        }
    }

    public static void InitEncoderController(Object Spark) {
        // motor.restoreFactoryDefaults();
        Devices.InitEncoderController(Spark,KP,KD,KI,MINOUT,MAXOUT,IZONE,FFVALUE,TARGETRPM,0,0);
    }

    public static void WriteIntakeStatus(String message) {
        if (isRunningTest) {
            System.out.println("Intake:"+message);
        }
        else {
            System.out.println("-Intake:"+message);
            SmartDashboard.putString("Intake:", message);
        }
    }

 /**
 * Detects the 2020 top vision target.
 * @param image The image from the camera.
 * @return The list of vision target groups.
 */
public Target detectBallTarget(Mat image){
    // Adjust these parameters for your team's needs
    Target foundTarget;

    if (image==null){System.err.println("image null");return null;}
    if (image.height()==0){System.err.println("image invalid");return null;}

    // Hue/Sat/Value filter parameters
    Range hsvHue = new Range(0, 170); // doctored image colors
    Range hsvSaturation = new Range(0, 255);
    Range hsvValue = new Range(54, 255);

//    Range hsvHue = new Range(15, 50); // cellphone colors
//    Range hsvSaturation = new Range(185, 255);
//    Range hsvValue = new Range(67, 255);
    
    // Contour filter parameters
    Range area = new Range(5, 1000);
    Range fullness = new Range(0, 1000);
    Range aspectRatio = new Range(0, 1000);
  
    // Camera settings
    FOV fov = new FOV(50, 40); // This is the approx. Microsoft LifeCam FOV
    Resolution resolution = new Resolution(720, 478);
    boolean cameraInverted = false;
  

    int imageArea = resolution.getArea();
  
    // An HSV filter may be better for FRC target detection
    CameraSettings cameraSettings = new CameraSettings(cameraInverted, fov, resolution);
    //TargetFilter targetFilter = new HSVFilter(new Range(50, 70), new Range(100, 255), new Range(100, 255));
    TargetFilter targetFilter = new HSVFilter(hsvHue, hsvSaturation, hsvValue);
    ContourFilter contourFilter = new StandardContourFilter( area, fullness, aspectRatio, imageArea);
    TargetGrouping targetGrouping = TargetGrouping.SINGLE;

    TargetFinder targetFinder = new TargetFinder(cameraSettings, targetFilter, contourFilter, targetGrouping);

    // Find targets
    List<Target> targets = targetFinder.findTargets(image);
    if (targets==null){System.err.println("no targets found");return null;}
    if (targets.size()==0){System.err.println("no targets found");return null;}
    
    // If the current target is a left and the next is a right, make it a pair
    for (int i = 0; i < targets.size(); i++) {
        //System.out.println(":"+targets.get(i).toString());
        //System.out.println("area:"+targets.get(i).getBoundary().size.area());
        //System.out.println("height:"+targets.get(i).getBoundary().size.height);
        //System.out.println("width:"+targets.get(i).getBoundary().size.width);
        if (targets.get(i).getBoundary().size.area() >= 1){
            if (targets.get(i).getPercentArea() >= 1){
                //if (targets.get(i).getVerticalAngle() <= 12){
                    //System.out.println("-->"+targets.get(i).toString());
                    foundTarget = targets.get(i);
                    return targets.get(i);
                //}
            }
        }
    }
    return null;
}
    private boolean isRunningTest() {
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
    
    public void SaveTargetImage(String name, Target target, Mat image) {
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
        }
        else {
            outputStream.putFrame(image);
        }
    }
}