package frc.robot;

import java.lang.reflect.*;

public class AutoControlData implements Cloneable {

    boolean WriteLog = true;
    AutoStates autoState = AutoStates.TeleOpt;
    GearStates gearState = GearStates.HighGearPressed;
    DeviceStates deviceState = DeviceStates.Drive;
    DriveStates driveState = DriveStates.Stop;
    LifterStates lifterState = LifterStates.Down;
    TargetStates targetState = TargetStates.TargetOff;


    Double LeftDriveSpeed=0.0;
    double LeftDrivePos=0.0;

    Double RightDriveSpeed=0.0;
    double RightDrivePos=0.0;

    public AutoControlData() {

    }
    public AutoControlData(AutoControlData data) {
        try{
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields){
                field.set(this,field.get(data));
            }
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public  String toString () {
        String sep = "";
        String ret = "";
        ret = "{";
        // ret = this.getClass().getSimpleName() + "={";
        try{
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields){
                ret = ret + sep + '"' +field.getName() + '"' + ":" + field.get(this);
                sep = ",";
            }
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
        return ret + "}";
    }
}