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

    private int imageIndex = 0;
    private String[] images = new String[10];
    public void initCamera()
    {
        OpenCVManager.getInstance().load(new SystemProperties());
        imageIndex = 0;
        if (images[0] !="TargetBall0.jpg") {
            images[0] ="TargetBall0.jpg";
            images[1] ="TargetBall1.jpg";
            images[2] ="TargetBall2.jpg";
            images[3] ="TargetBall3.jpg";
            images[4] ="TargetBall4.jpg";
            images[5] ="TargetBall5.jpg";
            images[6] ="TargetBall6.jpg";
        }
        else {
            images[0] ="2020target.jpg";
            images[1] ="IMG_20200304_202256262_HDR.jpg";
            images[2] ="IMG_20200304_202256263_HDR.jpg";
        }
    }

    private long lastSwitch;
    private boolean cam1=true;

    // function to return single image from the above stream
    public Mat getImage(){
        System.out.println(images[imageIndex]);
        String imageName = images[imageIndex++];
        Mat image = Imgcodecs.imread(imageName);
        //System.out.println(image);
        //Mat image = Imgcodecs.imread(images[imageIndex++]);
        return image;
    }
}

