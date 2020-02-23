package frc.robot;

import java.io.*;
import java.util.logging.FileHandler;
import java.lang.Math;

public class AutoRecordJson {
    public static java.util.Queue<AutoControlData> autodata;
    static Drive drive;
    static Shooter shooter;
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

    public AutoRecordJson(Devices devices2, Intake intake2, Lifter lifter2, Drive drive2, UserInput userinput2,Shooter shooter2) {
        shooter = shooter2;
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
        if (currentWriteLog && (previousWriteLog == true)) {
            previousWriteLog = false;
        }
        else if (WriteLog && currentWriteLog) {
            updateRecord(true);
            WriteJson();
            previousWriteLog = true;
            WriteLog = false;
        }
        else {
            WriteLog = true;
        }
        if (WriteLog) {
            // TODO write recording logic
                updateRecord(false);
                q = new AutoControlData(pq);
                autodata.add(q);
            }
        else {
            //System.out.println("Unchanged:" + cq.toString());
        }
        return autodata.size();
    }

    public static void updateRecord(boolean lastQueue){
        System.out.println(cq.toString());
        cq.gearState = drive.currentGearState;
        cq.LeftDriveSpeed = Devices.frontLeft.driveSpeed;
        cq.RightDriveSpeed = Devices.frontRight.driveSpeed;
        cq.LeftDrivePos = Devices.frontLeft.getPosition();
        cq.RightDrivePos = Devices.frontRight.getPosition();
        cq.intakeState = null; //intake.intakeStateMotor;
        cq.lifterState = lifter.lifterState;
        cq.shootingState = shooter.currentState;
        pq.shootingRamp++;
        //******************************* */
        // Time to write Drive data?
        //****************************** */
        if (cq.gearState != pq.gearState || 
            java.lang.Math.abs(cq.RightDriveSpeed - pq.LeftDriveSpeed) > .2) {
            AutoControlData wq = new AutoControlData();
            wq.LeftDrivePos = Devices.frontLeft.getPosition();
            wq.RightDrivePos = Devices.frontRight.getPosition();
            wq.LeftDriveSpeed = Devices.frontLeft.get();
            wq.RightDriveSpeed = Devices.frontRight.get();

            wq.LeftDrivePos = Devices.frontLeft.getPosition();
            wq.RightDrivePos = Devices.frontRight.getPosition();
            wq.LeftDriveSpeed = Devices.frontLeft.get();
            wq.RightDriveSpeed = Devices.frontRight.get();
            autodata.add(wq);
        }
        //******************************* */
        // Time to write Shooter data?
        //****************************** */
        if (cq.shootingState != pq.shootingState) {
            AutoControlData wq = new AutoControlData();
            wq.shootingState = cq.shootingState;
            wq.shootingRamp = cq.shootingRamp;
            pq.shootingRamp = 0;
            autodata.add(wq);
        }
        //******************************* */
        // Time to write Lifter data?
        //****************************** */
        if (cq.lifterState != pq.lifterState) {
            AutoControlData wq = new AutoControlData();
            wq.lifterState = pq.lifterState;
            wq.lifterLeftPos = Devices.lifter_left_motor.getPosition();
            wq.lifterRightPos = Devices.lifter_right_motor.getPosition();
            autodata.add(wq);
        }
        cq.Clone(cq, pq);
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