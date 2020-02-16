package frc.robot;

import java.io.*;
import java.util.logging.FileHandler;
import java.lang.Math;

public class AutoRecordJson {
    public static java.util.Queue<AutoControlData> autodata;
    static Drive drive;
    static Devices devices;
    static Intake intake;

    static Lifter lifter;
    static UserInput userinput;

    static AutoControlData cq;
    static AutoControlData pq; 
    static boolean WriteLog = false;
    static boolean previousWriteLog = false;

    static File jsonFile = null;
    static FileWriter jsonWriter;
    static File textFile = null;
    static FileWriter textWriter;

    public AutoRecordJson(Devices devices2, Intake intake2, Lifter lifter2, Drive drive2, UserInput userinput2) {
        devices = devices2;
        intake = intake2;
        lifter = lifter2;
        drive = drive2;
        userinput = userinput2;
        jsonFile = new File("autofile.json");
        textFile = new File("autofile.txt");
        cq = new AutoControlData();
        pq = new AutoControlData();
        autodata = new java.util.ArrayDeque<>();
        }

    public static int AutoRecorder() {
        Devices.frontLeft.updatePosition();
        Devices.frontRight.updatePosition();
        boolean currentWriteLog = UserInput.writeRecorder();
        if (currentWriteLog && previousWriteLog == false) {
            previousWriteLog = true;
        }
        else if (WriteLog && currentWriteLog) {
            WriteJson();
            previousWriteLog = false;
            WriteLog = false;
        }
        else {
            WriteLog = true;
        }
        if (WriteLog) {
            // TODO write recording logic
            cq.gearState = drive.currentGearState;
            cq.LeftDriveSpeed = Devices.frontLeft.driveSpeed;
            cq.RightDriveSpeed = Devices.frontRight.driveSpeed;
            cq.LeftDrivePos = Devices.frontLeft.getPosition();
            cq.RightDrivePos = Devices.frontRight.getPosition();
            cq.intakeStateMotor = null; //intake.intakeStateMotor;
            cq.intakeStateArms = null; //intake.intakeStateArms;
            cq.lifterState = null; //lifter.heldPosition;
            if (cq.gearState != pq.gearState || java.lang.Math.abs(cq.LeftDriveSpeed - pq.LeftDriveSpeed) > .2
                    || java.lang.Math.abs(cq.RightDriveSpeed - pq.LeftDriveSpeed) > .2
                    || cq.lifterState != pq.lifterState || cq.gearState != pq.gearState || cq.autoState != pq.autoState
                    || cq.intakeStateMotor != pq.intakeStateMotor || cq.intakeStateArms != pq.intakeStateArms) {
                AutoControlData q = new AutoControlData(cq);
                autodata.add(q);
            }
            else {
                System.out.println("Unchanged:" + cq.toString());
            }
            cq.Clone(cq, pq);
        }
        return autodata.size();
    }

    public static void WriteJson() {
        try {jsonWriter = new FileWriter(jsonFile, false);} 
            catch (IOException e) {e.printStackTrace();}
        try {textWriter = new FileWriter(textFile, false);} 
            catch (IOException e) {e.printStackTrace();}
        while (autodata.size() > 0) {
            cq = autodata.remove();
            try {jsonWriter.write(cq.toJson() + "\r\n");} 
                catch (IOException e) {e.printStackTrace();}
                try {textWriter.write(cq.toCode() + "\r\n");} 
                catch (IOException e) {e.printStackTrace();}
        }
        try {jsonWriter.close();}
            catch (IOException e) {e.printStackTrace();}
        try {textWriter.close();}
            catch (IOException e) {e.printStackTrace();}
    }
}       