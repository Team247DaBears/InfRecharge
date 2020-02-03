package frc.robot;

public class AutoQueue {
    public static final int MaxSize = 30;
    public static final int MaxDistance = 300;
    public static final Double MaxSpeed = 50.0;
    public static final Double CalcSpeed = -99999999.0;

    public static final java.util.Queue<AutoControlData> autodata = new java.util.ArrayDeque<>();
   
    public static int addQueue(AutoControlData qi)
    {
        AutoControlData q = new AutoControlData(qi);
        if (q.LeftDriveSpeed == CalcSpeed && q.RightDriveSpeed == CalcSpeed) {
            throw new ArithmeticException("Can't put -99999999L in both Speeds"); 
        }
        if (q.LeftDriveSpeed == CalcSpeed) {
            q.LeftDriveSpeed = q.LeftDriveCount / q.RightDriveCount * q.RightDriveSpeed;
        }
        else if (q.RightDriveSpeed == CalcSpeed) {
            q.RightDriveSpeed = q.LeftDriveCount / q.RightDriveCount * q.LeftDriveSpeed;
        }
        if (q.LeftDriveCount>MaxDistance) {
            throw new ArithmeticException("LeftDriveCount exceeded maxdistance of " + MaxDistance); 
        }
        if (q.RightDriveCount>MaxDistance) {
            throw new ArithmeticException("RightDriveCount exceeded maxdistance of " + MaxDistance); 
        }
        if (q.LeftDriveSpeed>MaxSpeed) {
            throw new ArithmeticException("LeftDriveSpeed exceeded maxdistance of " + MaxSpeed); 
        }
        if (q.RightDriveSpeed>MaxSpeed) {
            throw new ArithmeticException("RightDriveSpeed exceeded maxdistance of " + MaxSpeed); 
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