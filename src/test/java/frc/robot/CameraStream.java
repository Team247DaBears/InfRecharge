/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Add your docs here.
 */
public class CameraStream {

    public UsbCamera camera1;

    private CvSink cvSink;
    private CvSource outputStream;

    private int highimageIndex = 0;
    private int lowimageIndex = 0;
    private String[] highimages = new String[10];
    private String[] lowimages = new String[10];
    public void initCamera()
    {
        OpenCVManager.getInstance().load(new SystemProperties());
        highimageIndex = 0;
        lowimageIndex = 0;
            lowimages[0] ="TargetBall0.jpg";
            lowimages[0] ="2020target30inch.jpg";
            lowimages[1] ="TargetBall1.jpg";
            lowimages[1] ="2020target13inch.jpg";
            lowimages[2] ="TargetBall2.jpg";
            lowimages[3] ="TargetBall3.jpg";
            lowimages[4] ="TargetBall4.jpg";
            lowimages[5] ="TargetBall5.jpg";
            lowimages[6] ="TargetBall6.jpg";

            highimages[0] ="2020target.jpg";
            highimages[1] ="2020target2.jpg";
            highimages[2] ="2020target3.jpg";
            highimages[3] ="2020target4.jpg";
            highimages[4] ="2020target5.jpg";
    }

    private long lastSwitch;
    private boolean cam1=true;

    public Mat getHighImage(){
        System.out.println(highimages[highimageIndex]);
        String imageName = highimages[highimageIndex++];
        Mat image = Imgcodecs.imread(imageName);
        return image;
    }

    // function to return single image from the above stream
    public Mat getLowImage(){
        System.out.println(lowimages[lowimageIndex]);
        String imageName = lowimages[lowimageIndex++];
        Mat image = Imgcodecs.imread(imageName);
        return image;
    }
}

