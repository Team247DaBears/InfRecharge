package frc.robot;

public enum TargetStates implements Cloneable{
    TargetStart1, /* will only try targeting once */
    TargetStart2, /* will target twice */
    TargetStart3, /* will target three times */
    TargetOff /* turns off targeting */
}