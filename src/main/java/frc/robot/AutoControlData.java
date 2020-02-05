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


    Double LeftDriveSpeed=0.0;
    double LeftDrivePos=0.0;

    Double RightDriveSpeed=0.0;
    double RightDrivePos=0.0;

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
        this.LeftDrivePos = data.LeftDrivePos;
        this.RightDriveSpeed = data.RightDriveSpeed;
        this.RightDrivePos = data.RightDrivePos;
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