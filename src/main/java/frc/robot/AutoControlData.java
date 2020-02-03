package frc.robot;

import java.lang.reflect.*;

public class AutoControlData implements Cloneable {

    boolean WriteLog = true;
    AutoStates autoState = AutoStates.TeleOpt;
    GearStates gearState = GearStates.HighGearPressed;
    DeviceStates deviceState = DeviceStates.Drive;
    DriveStates driveState = DriveStates.Stop;
    LifterStates lifterState = LifterStates.Low;
    TargetStates targetState = TargetStates.TargetOff;


    int LeftDriveSpeed=0;
    int LeftDriveCount=0;

    int RightDriveSpeed=0;
    int RightDriveCount=0;

    public AutoControlData() {

    }
    public AutoControlData(AutoControlData data) {
        this.autoState = data.autoState;
        this.deviceState = data.deviceState;
        this.gearState = data.gearState;
        this.driveState = data.driveState;
        this.lifterState = data.lifterState;
        this.targetState = data.targetState;
        this.LeftDriveSpeed = data.LeftDriveSpeed;
        this.LeftDriveCount = data.LeftDriveCount;
        this.RightDriveSpeed = data.RightDriveSpeed;
        this.RightDriveCount = data.RightDriveCount;
    }

    @Override
    public  String toString () {
        String sep = "";
        String ret = "";
        ret = this.getClass().getSimpleName() + "={";
        try{
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields){
                ret = ret + sep + field.getName() + ":" + field.get(this);
                sep = ",";
            }
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
        return ret + "}";
    }
}