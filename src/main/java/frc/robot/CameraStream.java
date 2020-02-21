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

/**
 * Add your docs here.
 */
public class CameraStream {

    public UsbCamera camera1;

    private CvSink cvSink;
    private CvSource outputStream;

    public void initCamera()
    {
        System.out.println("About to init");
        camera1 = CameraServer.getInstance().startAutomaticCapture(0);
       // camera2 = CameraServer.getInstance().startAutomaticCapture(1);
        
      System.out.println("It didn't crash");
      camera1.setResolution(320, 240);
      camera1.setFPS(12);
      
      //  camera2.setResolution(320, 240);
        cvSink = CameraServer.getInstance().getVideo();
        outputStream = CameraServer.getInstance().putVideo("DriverCam", 320, 240);
        outputStream.setFPS(12);
        outputStream.setResolution(320, 240);
        //cvSink.setSource(camera1);
    }

    private long lastSwitch;
    private boolean cam1=true;

    // function to return single image from the above stream
    public Mat getImage(){
        Mat image = new Mat();
        //cvSink.grabFrameNoTimeout(image);
        int curBright = camera1.getBrightness();
        camera1.setResolution(720, 478);
        camera1.setExposureManual(3);
        //camera1.setWhiteBalanceManual(50);
        camera1.setBrightness(200);
        cvSink.grabFrame(image, 400);
        camera1.setExposureManual(curBright);
        camera1.setWhiteBalanceAuto();
        //camera1.setResolution(320, 240);
        //SmartDashboard.putBoolean("targetImage",(image != null));
        //SmartDashboard.putNumber("image width:",(double)image.width());
        //SmartDashboard.putNumber("image height:",(double)image.height());
        
        //cvSink.grabFrame(image);
        return image;
    }
}

