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
        previousWriteLog = false;
        WriteLog = false;
        jsonFile = new File("autofile.json");
        textFile = new File("autofile.txt");
        cq = new AutoControlData();
        pq = new AutoControlData();
        autodata = new java.util.ArrayDeque<>();
        }

    public static int AutoRecorder() {
        AutoControlData q;
        boolean currentWriteLog = UserInput.writeRecorder();
        System.out.println("currentWriteLog:"+currentWriteLog);
        System.out.println("previousWriteLog:"+previousWriteLog);
        System.out.println("WriteLog:"+WriteLog);
        if (currentWriteLog && (previousWriteLog == true)) {
            previousWriteLog = false;
        }
        else if (WriteLog && currentWriteLog) {
            updateRecord();
            q = new AutoControlData(pq);
            autodata.add(q);
            q = new AutoControlData(cq);
            autodata.add(q);
            WriteJson();
            previousWriteLog = true;
            WriteLog = false;
        }
        else {
            WriteLog = true;
        }
        if (WriteLog) {
            // TODO write recording logic
                updateRecord();
                q = new AutoControlData(pq);
                autodata.add(q);
            }
        else {
            //System.out.println("Unchanged:" + cq.toString());
        }
        return autodata.size();
    }

    public static void updateRecord(){
        System.out.println(cq.toString());
        cq.gearState = drive.currentGearState;
        cq.LeftDriveSpeed = Devices.frontLeft.driveSpeed;
        cq.RightDriveSpeed = Devices.frontRight.driveSpeed;
        cq.LeftDrivePos = Devices.frontLeft.getPosition();
        cq.RightDrivePos = Devices.frontRight.getPosition();
        cq.intakeStateMotor = null; //intake.intakeStateMotor;
        cq.intakeStateArms = null; //intake.intakeStateArms;
        cq.lifterState = lifter.lifterState;
        if (cq.gearState != pq.gearState || java.lang.Math.abs(cq.LeftDriveSpeed - pq.LeftDriveSpeed) > .2
                || java.lang.Math.abs(cq.RightDriveSpeed - pq.LeftDriveSpeed) > .2
                || cq.lifterState != pq.lifterState || cq.gearState != pq.gearState || cq.autoState != pq.autoState
                || cq.intakeStateMotor != pq.intakeStateMotor || cq.intakeStateArms != pq.intakeStateArms) {
            if (cq.lifterState != pq.lifterState) {
                pq.lifterPos = Devices.lifter_left_motor.getPosition();
                cq.lifterPos = Devices.lifter_left_motor.getPosition();
            }
            if (cq.gearState != pq.gearState || java.lang.Math.abs(cq.RightDriveSpeed - pq.LeftDriveSpeed) > .2) {
                pq.LeftDrivePos = Devices.frontLeft.getPosition();
                pq.RightDrivePos = Devices.frontRight.getPosition();
                pq.LeftDriveSpeed = Devices.frontLeft.get();
                pq.RightDriveSpeed = Devices.frontRight.get();

                cq.LeftDrivePos = Devices.frontLeft.getPosition();
                cq.RightDrivePos = Devices.frontRight.getPosition();
                cq.LeftDriveSpeed = Devices.frontLeft.get();
                cq.RightDriveSpeed = Devices.frontRight.get();
            }
        }
        System.out.println(cq.toString());
        cq.Clone(cq, pq);
        System.out.println(pq.toString());
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