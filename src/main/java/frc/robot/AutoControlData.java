package frc.robot;

import java.lang.reflect.*;
import java.util.regex.Pattern;


public class AutoControlData implements Cloneable {

    public boolean WriteLog = false;
    public AutoStates autoState = AutoStates.TeleOpt;
    public GearStates gearState = GearStates.LowGearPressed;
    public DeviceStates deviceState = DeviceStates.Drive;
    public DriveStates driveState = DriveStates.Stop;
    public LifterStates lifterState = LifterStates.LifterDown;
    public AutoShooterStates autoShooterState = AutoShooterStates.shooterIDLE;
    public double shooterDistance = 0.0;
    public double lifterLeftPos = 0;
    public double lifterRightPos = 0;
    public ShootingStates shootingState = ShootingStates.IDLE;
    public double shootingRamp = 0;
    public AutoIntakeStates autoIntakeState = AutoIntakeStates.intakeStop;
    public TargetStates targetState = TargetStates.TargetOff;
    public double targetLoop = 0;

    public Double CurrentDriveAngle=0.0;
    public Double TargetDriveAngle=999.999;
    public Double LeftDriveSpeed=0.0;
    public double LeftDrivePos=0.0;

    public Double RightDriveSpeed=0.0;
    public double RightDrivePos=0.0;
    
    public double distance = 0;
    public double height = 0;
    public double width = 0;
    public double horizontal = 0;
    public double virtical = 0;

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