package frc.robot;

import java.io.*;
import java.util.logging.FileHandler;
import java.lang.Math;

public class AutoRecordJson {
    public static java.util.Queue<AutoControlData> autodata = new java.util.ArrayDeque<>();
    static Drive drive;
    static Devices devices;
    static Intake intake;

    static Lifter lifter;
    static UserInput userinput;

    static AutoControlData cq = new AutoControlData();
    static AutoControlData pq = new AutoControlData();
    static boolean WriteLog = false;
    static boolean previousWriteLog = false;

    static File jsonFile = null;
    static FileWriter jsonWriter;

    public AutoRecordJson(Devices devices2, Intake intake2, Lifter lifter2, Drive drive2, UserInput userinput2) {
        devices = devices2;
        intake = intake2;
        lifter = lifter2;
        drive = drive2;
        userinput = userinput2;
        jsonFile = new File("autofile.txt");
    }

    public static void AutoRecorder() {
        boolean currentWriteLog = UserInput.writeRecorder();
        if (currentWriteLog && previousWriteLog == false) {
            previousWriteLog = true;
            if (WriteLog == true) {
                WriteLog = false;
            } else {
                WriteLog = true;
            }
        } else {
            WriteJson();
            previousWriteLog = false;
        }
        if (WriteLog) {
            // TODO write recording logic
            cq.gearState = drive.currentGearState;
            cq.intakeStateMotor = null; //intake.intakeStateMotor;
            cq.intakeStateArms = null; //intake.intakeStateArms;
            cq.lifterState = null; //lifter.heldPosition;
            if (cq.gearState != pq.gearState || java.lang.Math.abs(cq.LeftDriveSpeed - pq.LeftDriveSpeed) > .2
                    || java.lang.Math.abs(cq.RightDriveSpeed - pq.LeftDriveSpeed) > .2
                    || cq.lifterState != pq.lifterState || cq.gearState != pq.gearState || cq.autoState != pq.autoState
                    || cq.intakeStateMotor != pq.intakeStateMotor || cq.intakeStateArms != pq.intakeStateArms) {
                cq.LeftDrivePos = Devices.frontLeft.getPosition();
                cq.RightDrivePos = Devices.frontRight.getPosition();
                System.out.println("Queued:" + cq.toString());
                autodata.add(cq);
                System.out.println("Queued.Size:" + autodata.size());

            }
            else {
                System.out.println("Unchanged:" + cq.toString());
            }
            cq.Clone(pq, cq);
        }
    }

    public static void WriteJson() {
        try {
            jsonWriter = new FileWriter(jsonFile, false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (autodata.size() > 0) {
            cq = autodata.remove();
            try {
                jsonWriter.write(cq.toString() + "\r\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            jsonWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}       