package frc.robot;

import java.lang.reflect.*;
import java.util.regex.Pattern;


public class AutoControlData implements Cloneable {

    boolean WriteLog = true;
    AutoStates autoState = AutoStates.TeleOpt;
    GearStates gearState = GearStates.HighGearPressed;
    DeviceStates deviceState = DeviceStates.Drive;
    DriveStates driveState = DriveStates.Stop;
    LifterStates lifterState = LifterStates.LifterDown;
    double lifterLeftPos = 0;
    double lifterRightPos = 0;
    ShootingStates shootingState = ShootingStates.IDLE;
    double shootingRamp = 0;
    IntakeStates intakeState = IntakeStates.intakeStop;
    TargetStates targetState = TargetStates.TargetOff;
    double targetLoop = 0;

    Double LeftDriveSpeed=0.0;
    double LeftDrivePos=0.0;

    Double RightDriveSpeed=0.0;
    double RightDrivePos=0.0;
    
    double distance = 0;
    double height = 0;
    double width = 0;
    double horizontal = 0;
    double virtical = 0;

    public AutoControlData() {

    }
    public AutoControlData(AutoControlData data) {
        Clone(data,this);
    }

    /* copies values from source to clone */
    public void Clone(AutoControlData source, AutoControlData clone) {
        try{
            Field[] fields = clone.getClass().getDeclaredFields();
            for(Field field : fields){
                field.set(clone,field.get(source));
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
                ret = ret + sep + field.get(this);
                sep = ",";
            }
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
        return ret + "}";
    }

    public  String toCode () {
        String pattern = ".*[Ss]tate.*";

        String sep = "";
        String ret = "";
        ret = "AutoQueue.addQueue(";
        // ret = this.getClass().getSimpleName() + "={";
        try{
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields){
                ret = ret + sep;
                if (Pattern.matches(pattern,field.getName())) {
                    ret = ret + field.getType().toString().replace("class frc.robot.", "") + ".";
                }
                ret = ret + field.get(this);
                sep = ",";
            }
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
        return ret + ");";
    }
    public  String toJson () {
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