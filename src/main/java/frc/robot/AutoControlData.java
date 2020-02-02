package frc.robot;

public class AutoControlData implements Cloneable {

    AutoStates autoState;
    DeviceStates deviceState;
    GearStates gearState;
    LifterStates lifterState;
    TargetStates targetState;

    Long LeftDriveSpeed=0L;
    Long LeftDriveCount=0L;

    Long RightDriveSpeed=0L;
    Long RightDriveCount=0L;

    public AutoControlData() {

    }
    public AutoControlData(AutoControlData data) {
        this.autoState = data.autoState;
        this.deviceState = data.deviceState;
        this.gearState = data.gearState;
        this.lifterState = data.lifterState;
        this.targetState = data.targetState;
        this.LeftDriveSpeed = data.LeftDriveSpeed;
        this.LeftDriveCount = data.LeftDriveCount;
        this.RightDriveSpeed = data.RightDriveSpeed;
        this.RightDriveCount = data.RightDriveCount;
    }
}