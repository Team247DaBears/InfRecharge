package frc.robot;

public class AutoQueue {
    public static final int MaxSize = 30;
    public static final int MaxDistance = 300;
    public static final int MinDistance = 0;
    public static final Double MaxSpeed = 1.0;
    public static final Double MinSpeed = -1.0;
    public static final Double CalcSpeed = -99999999.0;

    public static java.util.Queue<AutoControlData> autodata = new java.util.ArrayDeque<>();
   
    /**
   * Queue AutoShoot States. 
   * @param autoState Automous Shooter state
   * @param ShootingStates State of the Shooter
   * @param ShootingRamp Number of cycles before changing state
   * @return int number of entries in the queue
   */
    public static int addShooterQueue(AutoStates autostate,
        ShootingStates shootingstates,
        Double shootingramp) {
        AutoControlData q = new AutoControlData();
        q.autoState = autostate;
        q.shootingState = shootingstates;
        q.shootingRamp = shootingramp;
        return addQueue(q);
        }
    /**
   * Queue AutoShoot States. 
   * @param autoState Automous Drive state
   * @param DriveState State of the Drive
   * @param GearState State of the Gear
   * @param LeftSpeed speed (double) of left track
   * @param LeftPos PID position (double)
   * @param RightSpeed speed (double) of left track
   * @param RightPos PID Position (double) 
   * @return int number of entries in the queue
   */
    public static int addDriveQueue(AutoStates autostate,
        DriveStates drivestate,
        GearStates gearstate,
        Double leftdrivespeed,
        Double leftdrivepos,
        Double rightdrivespeed,
        Double rightdrivepos) {
        AutoControlData q = new AutoControlData();
        q.autoState = autostate;
        q.gearState = gearstate;
        q.driveState = drivestate;
        q.LeftDriveSpeed = leftdrivespeed;
        q.LeftDrivePos = leftdrivepos;
        q.RightDriveSpeed = rightdrivespeed;
        q.RightDrivePos = rightdrivepos;
        return addQueue(q);
        }
    /**
   * Queue AutoTarget States. 
   * @param targetState Automous targeting
   * @return int number of entries in the queue
   */
  public static int addTargetQueue(AutoStates autostate,
        TargetStates targetstate,
        double targetloop
        ) {
        AutoControlData q = new AutoControlData();
        q.autoState = autostate;
        q.targetState = targetstate;
        q.targetLoop = targetloop;
        q.distance = DetectTarget.default_distance;
        q.height = DetectTarget.default_height;
        q.width = DetectTarget.default_width;
        q.horizontal = DetectTarget.default_horizontal;
        q.virtical = DetectTarget.default_virtical;
        return addQueue(q);
        }
    /**
   * Queue AutoTarget States. 
   * @param targetState Automous targeting
   * @return int number of entries in the queue
   */
  public static int addTargetQueue(AutoStates autostate,
        TargetStates targetstate,
        double targetloop,
        double distance,
        double height,
        double width,
        double horizontal,
        double virtical) {
        AutoControlData q = new AutoControlData();
        q.autoState = autostate;
        q.targetState = targetstate;
        q.targetLoop = targetloop;
        q.distance = distance;
        q.height = height;
        q.width = width;
        q.horizontal = horizontal;
        q.virtical = virtical;
        return addQueue(q);
        }
    /**
   * Queue ALL AUTOSTATES States. 
   * @param autoState Automous Shooter state
   * @param lots below....
   * @return int number of entries in the queue
   */
  public static int addQueue(AutoStates autostate,
        GearStates gearstate,
        DriveStates drivestate,
        LifterStates lifterstate,
        double lifterleftpos,
        double lifterrightpos,
        IntakeStates intakestate,
        TargetStates targetstate,
        Double leftdrivespeed,
        Double leftdrivepos,
        Double rightdrivespeed,
        Double rightdrivepos) {
        AutoControlData q = new AutoControlData();
        q.autoState = autostate;
        q.gearState = gearstate;
        q.driveState = drivestate;
        q.lifterState = lifterstate;
        q.lifterLeftPos = lifterleftpos;
        q.lifterRightPos = lifterrightpos;
        q.intakeState = intakestate;
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
    public static int moveFirst() {
        java.util.ArrayDeque<AutoControlData> t;
        t = (java.util.ArrayDeque<AutoControlData>)autodata;
        t.addFirst(t.getLast());
        t.removeLast();
        return autodata.size();
    }
}