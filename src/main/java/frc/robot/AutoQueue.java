package frc.robot;

public class AutoQueue {
    public static final int MaxSize = 30;
    public static final int MaxDistance = 300;
    public static final int MinDistance = 0;
    public static final Double MaxSpeed = 1.0;
    public static final Double MinSpeed = -1.0;
    public static final Double CalcSpeed = -99999999.0;

    public static java.util.Queue<AutoControlData> autodata = new java.util.ArrayDeque<>();
   
    public static int addQueue(boolean writelog,
        AutoStates autostate,
        GearStates gearstate,
        DeviceStates devicestate,
        DriveStates drivestate,
        LifterStates lifterstate,
        IntakeStates intakestate,
        IntakeStates intakearmstate,
        TargetStates targetstate,
        Double leftdrivespeed,
        Double leftdrivepos,
        Double rightdrivespeed,
        Double rightdrivepos) {
        AutoControlData q = new AutoControlData();
        q.autoState = autostate;
        q.gearState = gearstate;
        q.deviceState = devicestate;
        q.driveState = drivestate;
        q.lifterState = lifterstate;
        q.intakeStateMotor = intakestate;
        q.intakeStateArms = intakearmstate;
        q.targetState = targetstate;
        q.LeftDriveSpeed = leftdrivespeed;
        q.LeftDrivePos = leftdrivepos;
        q.RightDriveSpeed = rightdrivespeed;
        q.RightDrivePos = rightdrivepos;
        return addQueue(q);
        }
    public static int addQueue(AutoControlData qi)
    {
        AutoControlData q = new AutoControlData(qi);
        if (q.LeftDriveSpeed == CalcSpeed && q.RightDriveSpeed == CalcSpeed) {
            throw new ArithmeticException("Can't put -99999999L in both Speeds"); 
        }
        if (q.LeftDriveSpeed == CalcSpeed) {
            q.LeftDriveSpeed = q.LeftDrivePos / q.RightDrivePos * q.RightDriveSpeed;
        }
        else if (q.RightDriveSpeed == CalcSpeed) {
            q.RightDriveSpeed =  q.RightDrivePos / q.LeftDrivePos * q.LeftDriveSpeed;
        }
        if (q.LeftDrivePos>MaxDistance) {
            throw new ArithmeticException("LeftDrivePos exceeded maxdistance of " + MaxDistance); 
        }
        if (q.RightDrivePos>MaxDistance) {
            throw new ArithmeticException("RightDrivePos exceeded maxdistance of " + MaxDistance); 
        }
        if (q.LeftDrivePos<MinDistance) {
            throw new ArithmeticException("LeftDrivePos below mindistance of " + MinDistance); 
        }
        if (q.RightDrivePos<MinDistance) {
            throw new ArithmeticException("RightDrivePos below mindistance of " + MinDistance); 
        }
        if (q.LeftDriveSpeed>MaxSpeed) {
            throw new ArithmeticException("LeftDriveSpeed " + q.LeftDriveSpeed + " exceeded maxspeed of " + MaxSpeed); 
        }
        if (q.RightDriveSpeed>MaxSpeed) {
            throw new ArithmeticException("RightDriveSpeed " + q.RightDriveSpeed + " exceeded maxspeed of " + MaxSpeed); 
        }
        if (q.LeftDriveSpeed<MinSpeed) {
            throw new ArithmeticException("LeftDriveSpeed "+q.LeftDriveSpeed+" exceeded maxspeed of " + MinSpeed); 
        }
        if (q.RightDriveSpeed<MinSpeed) {
            throw new ArithmeticException("RightDriveSpeed "+q.RightDriveSpeed+" below minspeed of " + MinSpeed); 
        }
        if (autodata.size()>=MaxSize) {
            throw new ArithmeticException("Queue exceeds Max Size of " + MaxSize); 
        }
        autodata.add(q);
        return autodata.size(); // return size of queue
    }

    public static AutoControlData getQueue()
    {
        AutoControlData tempStateData = null;
        if (autodata.size() > 0) {
            tempStateData = autodata.poll();
        }

        if (tempStateData == null) {
            AutoControlData tempTeleOpt = new AutoControlData();
            tempTeleOpt.autoState = AutoStates.TeleOpt;
            tempTeleOpt.WriteLog = false;
            return tempTeleOpt;
        }
        return tempStateData;
    }

    public static int removeCurrent()
    {
        if (autodata.size() > 0) {
            autodata.remove();
        }
        return autodata.size();
    }

    public static int clearQueue()
    {
        if (autodata.size() > 0) {
            autodata.removeAll(autodata);
        }
        return autodata.size();
    }

    public static AutoControlData currentQueue()
    {
        AutoControlData tempStateData;
        tempStateData = autodata.peek();

        if (tempStateData == null) {
            AutoControlData tempTeleOpt = new AutoControlData();
            tempTeleOpt.autoState = AutoStates.TeleOpt;
            tempTeleOpt.WriteLog = false;
            return tempTeleOpt;
        }
        return tempStateData;
    }

    public static java.util.Queue<AutoControlData> waitingQueue()
    {
        return autodata;
    }

    public static int getSize()
    {
        return autodata.size();
    }

}