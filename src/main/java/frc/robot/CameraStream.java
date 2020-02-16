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

/**
 * Add your docs here.
 */
public class CameraStream {

    private UsbCamera camera1;

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
        cvSink.setSource(camera1);
    }

    private long lastSwitch;
    private boolean cam1=true;

    // function to return single image from the above stream
    public Mat getImage(){
        Mat image = new Mat();
        cvSink.grabFrame(image);
        return image;
    }
}

