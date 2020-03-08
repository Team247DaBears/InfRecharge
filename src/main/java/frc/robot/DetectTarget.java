package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;

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
import com.kylecorry.frc.vision.targetConverters.TargetUtils;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.List;

//

public class DetectTarget {
    double distance;
    double height;
    double width;
    double horizontal;
    double virtical;
    static final double okIntake =0.1; //ok intake final distance diff
    static final double okDist =0.1; //ok distance diff
    static final double okVirt =0.1; //ok Vertical diff
    static final double okHztl =0.1; //ok Horizontal diff

    static final double hztlAdj =1; //adjust horizontal position ONE INCH
    static final double virtAdj =1; //adjust horizontal position ONE INCH
    static final double default_distance =8.0; //top centered target
    static final double default_height =0.0; //top centered target
    static final double default_width = 0.0; //top centered target
    static final double default_horizontal = 0.0; //target location
    static final double default_virtical = 10; //target location

    CameraStream camerastream;
    CvSource outputStream;
    private Boolean isRunningTest = null;

    public void Init(CameraStream camera) {
        distance = default_distance;
        height = default_height;
        width = default_width;
        horizontal = default_horizontal;
        virtical = default_virtical;
    
 //       SmartDashboard.putNumber("camExp:", 4);
 //       SmartDashboard.putNumber("camBal:", 100);
//        camerastream = camera;
//        outputStream = CameraServer.getInstance().putVideo("TargetCam", 320, 240);
//        outputStream.setFPS(12);
//        outputStream.setResolution(320, 240);
    }

 /**
 * Shoots at top target.
 * @param image The image from the camera.
 * @return The list of vision target groups.
 */
    public Boolean shootTopTarget() {
        if (UserInput.getTarget()) {
            if (camerastream != null){
                return shootTarget(camerastream.getImage());
            }
            else {
                return false;
            }
    }
        else {
            return false;
        }
    }

    public Boolean shootTarget(Mat image) {
        if (Devices.frontLeft.get() == 0.0 && Devices.frontRight.get() == 0.0) {
            if (image != null) {
            if (image.height()>0 && image.width() >0){
                if (targetTopTarget(image)) {
                // Shoot the ball by starting autoshoot            
                AutoQueue.addShooterQueue(AutoStates.Target,ShootingStates.IDLE,10.0);        
                AutoQueue.moveFirst(); /* move shooter to first */
                return true;
                }
                else {
                    SmartDashboard.putString("Targetting","No Target Found. You must see it to push the button.");
                    return false;
                }
            }
            else {
                SmartDashboard.putString("Targetting","Image Capture Failed. Try again ... press the button?");
                return false;
            }
        }
        else {
            SmartDashboard.putString("Targetting","Image Capture Failed. Try again ... press the button?");
            return false;
        }
    }
    else {
        SmartDashboard.putString("Targetting","Don't move robot while trying to target.");
        return false;
    }
}
/**
 * Moves Robot to target top vision target.
 * @param image The image from the camera.
 * @return The list of vision target groups.
 */
public Boolean targetTopTarget(Mat image) {

    if (image==null){System.err.println("no image provided (null)");return false;} // image null (not set)
    if (image.height()==0){System.err.println("image invalid");return false;} // image empty

    Target current = detectTopTarget(image);
    //System.out.println("target:"+current.toString());
    if (current==null) {System.err.println("no target found"); return false;} // no target found

    Size size = current.getBoundary().size;
    FixedAngleCameraDistanceEstimator distanceEstimator = 
        new FixedAngleCameraDistanceEstimator(10, 7, 10);
    Double currdistance = distanceEstimator.getDistance(current);
    Double currvirt = current.getVerticalAngle();
    Double currhztl = current.getHorizontalAngle();
    Double diffDist = currdistance - distance;
    Double diffVirt = currvirt - virtical;
    //System.out.println("distance:"+distance);
    if (Math.abs(diffDist) > okIntake /* Target Size too Big */) {
            AutoQueue.addDriveQueue(AutoStates.Drive,DriveStates.DriveStart,GearStates.LowGearPressed,0.5,diffDist,0.5,diffDist);
            AutoQueue.moveFirst();
        }
    else  if (Math.abs(diffVirt) > okVirt/*target too far left */) {
                AutoQueue.addDriveQueue(AutoStates.Drive,DriveStates.DriveStart,GearStates.LowGearPressed,.5,diffVirt*virtAdj,.5,-diffVirt*virtAdj);
                AutoQueue.moveFirst();
        }
    else {
            AutoQueue.addShooterQueue(AutoStates.Shooter,ShootingStates.HIGHSHOT,10.0);    
            AutoQueue.moveFirst();
            return true; /*fire*/
        }
    return false;
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
    Range hsvHue = new Range(15, 50);
    Range hsvSaturation = new Range(185, 255);
    Range hsvValue = new Range(67, 255);
    
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
        System.out.println(":"+targets.get(i).toString());
        System.out.println("area:"+targets.get(i).getBoundary().size.area());
        System.out.println("height:"+targets.get(i).getBoundary().size.height);
        System.out.println("width:"+targets.get(i).getBoundary().size.width);
        if (targets.get(i).getBoundary().size.area() >= 1){
            if (targets.get(i).getPercentArea() >= 1){
            if (targets.get(i).getVerticalAngle() <= 12){
                //System.out.println("-->"+targets.get(i).toString());
                foundTarget = targets.get(i);
                return targets.get(i);
            }
        }
    }
}
    
    return null;
  }
  
/**
 * Detects the 2020 top vision target.
 * @param image The image from the camera.
 * @return The list of vision target groups.
 */
public Target detectTopTarget(Mat image){
    // Adjust these parameters for your team's needs
    Target foundTarget;

    if (image==null){System.err.println("image null");return null;}
    if (image.height()==0){System.err.println("image invalid");return null;}

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
        if (targets.get(i).getPercentArea() >= 1){
            if (targets.get(i).getVerticalAngle() >= 10){
                //System.out.println("-->"+targets.get(i).toString());
                foundTarget = targets.get(i);
                SaveTargetImage("findTopTarget", foundTarget, image);
                return targets.get(i);
            }
        }
    }
    
    return null;
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

public void AutoTarget() {
    if (AutoQueue.getSize() == 0) {return;}
    AutoControlData q = AutoQueue.currentQueue();
    switch(q.targetState){
        case TargetStart1:
            q.targetState = TargetStates.TargetOff;
            if (shootTopTarget()) {
                q.targetState = TargetStates.TargetOff;
            }
            break;
        case TargetStart2:
           q.targetState = TargetStates.TargetStart1;
           if (shootTopTarget()) {
            q.targetState = TargetStates.TargetOff;
            }
            break;
        case TargetStart3:
            q.targetState = TargetStates.TargetStart2;
            if (shootTopTarget()) {
                q.targetState = TargetStates.TargetOff;
            }
            break;
        case TargetOff:
            AutoQueue.removeCurrent();
            break;
    }
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


}